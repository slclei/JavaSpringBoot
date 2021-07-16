'use strict';

const React=require('react');
const ReactDOM=require('react-dom');
//3.23
const client = require('./client');
const follow=require('./follow');
const when=require('when');

var stompClient=require('./websocket-listener')

var root='/api';

//create a React component
//with state of all students
//render the component on the screen
class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {students: [], attributes: [], page: 1, pageSize: 2, links: {}};
        this.updatePageSize = this.updatePageSize.bind(this);
        this.onCreate = this.onCreate.bind(this);
        this.onUpdate=this.onUpdate.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.onNavigate = this.onNavigate.bind(this);
        this.refreshAndGoToLastPage=this.refreshAndGoToLastPage.bind(this);
        this.refreshCurrentPage=this.refreshCurrentPage.bind(this);
    }

    //React renders a component in the DOM
    componentDidMount() {
        this.loadFromServer(this.state.pageSize);
        stompClient.register([
        {route:'/topic/newStudent',callback:this.refreshAndGoToLastPage},
        {route:'/topic/updateStudent',callback: this.refreshCurrentPage},
        {route:'/topic/deleteStudent',callback: this.refreshCurrentPage}
        ]);
    }

    //reload list with updated page
    loadFromServer(pageSize){
        follow(client, root, [
            {rel: 'students', params:{size: pageSize}}
            //fetch JSON schema data
        ]).then(studentCollection =>{
            return client({
                method:'GET',
                path: studentCollection.entity._links.profile.href,
                headers:{"Accept":'application/schema+json'}
                //store metadata and navigational links in the <App/> component
                //return collection, and pass this collection onto the next call
            }).then(schema=>{
                this.schema=schema.entity;
                this.links=studentCollection.entity._links;
                return studentCollection;
            });
            //fetch each individual resource, including an ETag header
        }).then(studentCollection => {
            //event
            this.page=studentCollection.entity.page;
            return studentCollection.entity._embedded.students.map(student =>
            client({
                method:'GET',
                path:student._links.self.href
                  })
            );
            //takes the array of GET promises, and merges them into a single promise
        }).then(studentPromises => {
            return when.all(studentPromises);
        }).done(students =>{
            this.setState({
                //event
                page:this.page,
                students:students,
                attributes: Object.keys(this.schema.properties),
                pageSize:pageSize,
                links: this.links
            });
        })
    }

    //on create event to handle create a new student
    onCreate(newStudent){
        follow(client,root,['students']).then(response=>{
            client({
                method:'POST',
                path: response.entity._links.self.href,
                entity: newStudent,
                headers:{'Content-Type':'application/json'}
            })
        })
    }

    //tag::update[]
    onUpdate(student,updatedStudent){
        client({
            method: 'PUT',
            path: student.entity._links.self.href,
            entity: updatedStudent,
            headers:{
                'Content-Type': 'application/json',
                'If-Match': student.headers.Etag
            }
        }).done(response => {
            //event. let the websorkect handler update the state
        }, response =>{
            if (response.status.code ===412){
                alert('Denied: unable to update'+
                student.entity._links.self.href +'. Your copy is stale.');
            }
            else{
                alert('Denied: unable to update'+response.status.code+
                    student.entity._links.self.href +'. Your copy is stale.');
            }
        });
    }
    //end::update[]

    //navigate event to go to target url
    onNavigate(navUri) {
        client({
            method: 'GET',
            path: navUri
        }).then(studentCollection =>{
            this.links=studentCollection.entity._links;
            //event
            this.page=studentCollection.entity.page;

            return studentCollection.entity._embedded.students.map(student =>
            client({
                method:'GET',
                path: student._links.self.href
            }));
        }).then(studentPromises =>{
            return when.all(studentPromises);
        }).done(students => {
            this.setState({
                page:this.page,
                students:students,
                attributes: Object.keys(this.schema.properties),
                pageSize:this.state.pageSize,
                links: this.links
            });
        });
    }

    onDelete(student){
        client({method:'DELETE',path: student.entity._links.self.href});

    }

    updatePageSize(pageSize){
        if (pageSize!==this.state.pageSize){
            this.loadFromServer(pageSize);
        }
    }

    //tag::refresh and go to last page
    refreshAndGoToLastPage(message) {
        follow(client,root, [{
            rel:'students',
            params:{size:this.state.pageSize}
        }]).done(response => {
            if (response.entity._links.last!==undefined) {
                this.onNavigate(response.entity._links.last.href);
            } else {
                this.onNavigate(response.entity._links.self.href);
            }
        })
    }
    //end::refresh and go to last page

    //tag::refresh and go to last page
    refreshCurrentPage(message) {
        follow(client,root,[{
            rel:'students',
            params:{
                size:this.state.pageSize,
                page:this.state.page.number
            }
        }]).then(studentCollection => {
            this.links=studentCollection.entity._links;
            this.page=studentCollection.entity.page;

            return studentCollection.entity._embedded.students.map(student=>{
                return client({
                    method:'GET',
                    path:student._links.self.href
                })
            });
        }).then(studentPromises => {
            return when.all(studentPromises);
        }).then(students => {
            this.setState({
                page:this.page,
                students:students,
                attributes:Object.keys(this.schema.properties),
                pageSize:this.state.pageSize,
                links:this.links
            });
        });
    }
    //end::refresh and go to last page
        //draws the component
    render(){
        return(
            <div>
                <CreateDialog attributes={this.state.attributes} onCreate={this.onCreate}/>
                <StudentList  page={this.state.page}
                                students={this.state.students}
                              links={this.state.links}
                              pageSize={this.state.pageSize}
                             attributes={this.state.attributes}
                              onNavigate={this.onNavigate}
                             onUpdate={this.onUpdate}
                              onDelete={this.onDelete}
                              updatePageSize={this.updatePageSize}/>
            </div>
        )
    }
}

//create new student in a dialog
class CreateDialog extends React.Component{
    constructor(props) {
        super(props);
        this.handleSubmit=this.handleSubmit.bind(this);
    }

    handleSubmit(e){
        //prevent default for e
        e.preventDefault();
        const newStudent={};
        //set all attributes for new student record
        this.props.attributes.forEach(attribute =>{
            newStudent[attribute]=
                ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        //create new student record
        this.props.onCreate(newStudent);

        //clear out the dialog's inputs for next input
        this.props.attributes.forEach(attribute=>{
            ReactDOM.findDOMNode(this.refs[attribute]).value='';
        });

        //Navigate away from the dialog to hide it
        window.location='#';
    }

    //render this dialog
    render(){
        //create an html component of inputs, with default placeholder as the attribute name
        //converts the attirbutes property into an array of input elements
        //ref is used to be grabbed
        //dynamic loading data from server
        const inputs=this.props.attributes.map(attribute=>
            <p key={attribute}>
                <input type="text" placeholder={attribute} ref=
                    {attribute} className="field" />
            </p>);

        //create a new dialog to create new student
        //
        return (
            //this div anchor tag is the button to open the dialog
            //3rd nested div is the dialog itself
            //2nd nested div is a form with dynamic list of input fields
            <div>
                <a href="#createStudent">Create</a>
                <div id="createStudent" className="modalDialog">
                    <div>
                        <a href="#" title="Close" className="close">X</a>
                        <h2>Create new student</h2>
                        <form>
                            {inputs}
                            <button onClick={this.handleSubmit}>Create</button>
                        </form>
                    </div>
                </div>
            </div>
        )
    }
}

//tag::updateDialog
class UpdateDialog extends React.Component{
    constructor(props) {
        super(props);
        this.handleSubmit=this.handleSubmit.bind(this);
    }

    handleSubmit(e){
        e.preventDefault();
        const updatedStudent={};
        this.props.attributes.forEach(attribute => {
            updatedStudent[attribute]=
                ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onUpdate(this.props.student,updatedStudent);
        window.location='#';
    }

    render(){
        const inputs=this.props.attributes.map(attribute =>
        <p key={this.props.student.entity[attribute]}>
            <input type="text" placeholder={attribute} defaultValue={this.props.student.entity[attribute]}
            ref={attribute} className="field"/>
        </p>
        );
        const dialogId="updateStudent-"+this.props.student.entity._links.self.href;

        return (
            <div key={this.props.student.entity._links.self.href}>
                <a href={"#"+dialogId}>Update</a>
                <div id={dialogId} className="modalDialog">
                    <div>
                        <a href="#" title="Close" className="close">X</a>

                        <h2>Update a student</h2>

                        <form>
                            {inputs}
                            <button onClick={this.handleSubmit}>Update</button>
                        </form>
                    </div>
                </div>
            </div>
        )
    };
}
//end::updateDialog

class StudentList extends React.Component{
    constructor(props) {
        super(props);
        //after bind this to the specific function
        //this is binded to this object
        //otherwise, this.props.onNavigate(this.props.links.last.href);
        //will have two different this
        this.handleNavFirst=this.handleNavFirst.bind(this);
        this.handleNavPrev=this.handleNavPrev.bind(this);
        this.handleNavNext=this.handleNavNext.bind(this);
        this.handleNavLast=this.handleNavLast.bind(this);
        this.handleInput=this.handleInput.bind(this);
    }

    // tag:: handle-page-size-updates[]
    handleInput(e){
        e.preventDefault();
        const pageSize=ReactDOM.findDOMNode(this.refs.pageSize).value;
        if (/^[0-9]+$/.test(pageSize)) {
            this.props.updatePageSize(pageSize);
        } else {
            ReactDOM.findDOMNode(this.refs.pageSize).value=
                pageSize.substring(0,pageSize.length-1);
        }
    }
    // end:: handle-page-size-updates[]

    // tag::handle-nav[]
    handleNavFirst(e){
        e.preventDefault();
        this.props.onNavigate(this.props.links.first.href);
    }

    handleNavPrev(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.prev.href);
    }

    handleNavNext(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.next.href);
    }

    handleNavLast(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.last.href);
    }
    // end::handle-nav[]

    //render final student list
    render(){
        const students=this.props.students.map(student =>
            <Student key={student.entity._links.self.href} student={student}
                     attributes={this.props.attributes}
                     onUpdate={this.props.onUpdate}
                     onDelete={this.props.onDelete}/>
        );

        const navLinks=[];
        if ("first" in this.props.links) {
            navLinks.push(<button key="first" onClick={this.handleNavFirst}>&lt;&lt;</button>);
        }
        if ("prev" in this.props.links) {
            navLinks.push(<button key="prev" onClick={this.handleNavPrev}>&lt;</button>);
        }
        if ("next" in this.props.links) {
            navLinks.push(<button key="next" onClick={this.handleNavNext}>&gt;</button>);
        }
        if ("last" in this.props.links) {
            navLinks.push(<button key="last" onClick={this.handleNavLast}>&gt;&gt;</button>);
        }

        return (
            <div>
                <input ref="pageSize" defaultValue={this.props.pageSize} onInput={this.handleInput}/>
                <table>
                    <tbody>
                    <tr>
                        <th>Name</th>
                        <th>Update</th>
                        <th>Del</th>
                    </tr>
                    {students}
                    </tbody>
                </table>
                <div>
                    {navLinks}
                </div>
            </div>
        )
    }
}

class Student extends React.Component{
    constructor(props) {
        super(props);
        this.handleDelete=this.handleDelete.bind(this);
    }

    handleDelete(){
        this.props.onDelete(this.props.student);
    }

    //attributes are only for those in curl shown
    render() {
        return (
            <tr>
                <td>{this.props.student.entity.name}</td>
                <td>
                    <UpdateDialog student={this.props.student}
                    attributes={this.props.attributes}
                    onUpdate={this.props.onUpdate}/>
                </td>
                <td>
                    <button onClick={this.handleDelete}>Delete</button>
                </td>
            </tr>
        )
    }
}

ReactDOM.render(
    <App />,
    document.getElementById('react')
)