var Container = React.createClass({displayName: 'Container',
	getInitialState: function(){
		return {data: []};
	},
	render: function(){
		return(
				Header(null)
				//NavBar(null),
				//Content(null)
			);
	}
});
var Header = React.createClass({displayName:'Header',
	getInitialState: function(){
		return {data: []};
	},
	render: function(){
		return(
			React.DOM.div({id:'header', className:'row'},
				React.DOM.div({id:'logo', className:'small-10 medium-3 columns'},
					React.DOM.h1(null, "mvMusic")
					),
				React.DOM.div({id:'search',className:'small-10 medium-7 columns'},
					Search(null)
				)
			)
		);
	}
});
var Search = React.createClass({displayName:'Search',
	render: function(){
		return(
				React.DOM.form({className:'searchForm'},
					React.DOM.div({className:'row'},
						React.DOM.div({className:'small-10 medium-7 columns'},
							React.DOM.input({
								type: "text",
								placeholder: "search",
								ref: "searchbox"
								})
							),
						React.DOM.div({className:'small-10 medium-3 columns'},
							React.DOM.input({type:"submit", value:"Search"})
							)
						)
				)
			);
	}
});

React.renderComponent(
		Container(null),
		document.getElementById('content')
		);
