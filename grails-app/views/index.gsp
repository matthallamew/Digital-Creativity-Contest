<!doctype html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<style type="text/css" media="screen">
			#status {
				background-color: #eee;
				border: .2em solid #fff;
				margin: 2em 2em 1em;
				padding: 1em;
				width: 12em;
				float: right;
				-moz-box-shadow: 0px 0px 1.25em #ccc;
				-webkit-box-shadow: 0px 0px 1.25em #ccc;
				box-shadow: 0px 0px 1.25em #ccc;
				-moz-border-radius: 0.6em;
				-webkit-border-radius: 0.6em;
				border-radius: 0.6em;
			}

			.ie6 #status {
				display: inline; /* float double margin fix http://www.positioniseverything.net/explorer/doubled-margin.html */
			}

			#status ul {
				font-size: 0.9em;
				list-style-type: none;
				margin-bottom: 0.6em;
				padding: 0;
			}
            
			#status li {
				line-height: 1.3;
			}

			#status h1 {
				text-transform: uppercase;
				font-size: 1.1em;
				margin: 0 0 0.3em;
			}

			#pop {
				background-color: #eee;
				border: .2em solid #fff;
				margin: 1em 9em 1em 4em;
				padding: 2em;
				float: right;
				-moz-box-shadow: 0px 0px 1.25em #ccc;
				-webkit-box-shadow: 0px 0px 1.25em #ccc;
				box-shadow: 0px 0px 1.25em #ccc;
				-moz-border-radius: 0.6em;
				-webkit-border-radius: 0.6em;
				border-radius: 0.6em;
			}

			.ie6 #pop {
				display: inline; /* float double margin fix http://www.positioniseverything.net/explorer/doubled-margin.html */
			}

			#page-body {
				margin: 1em 0 1.25em 6em;
			}

			h2 {
				margin-top: 1em;
				margin-bottom: 0.3em;
				font-size: 1em;
			}

			p {
				line-height: 1.5;
				margin-right: 8em;
			}

			#controller-list ul {
				list-style-position: inside;
			}

			#controller-list li {
				line-height: 1.3;
				list-style-position: inside;
				margin: 0.25em 0;
			}

			@media screen and (max-width: 480px) {
				#status {
					display: none;
				}

				#page-body {
					margin: 0 .25em 1em;
				}
				p {
					margin-right: 1em;
					margin-left: .25em;
				}

				#page-body h1 {
					margin-top: 0;
				}
				#DCCLogo {
					padding-top: 5px;
				}
			}
		</style>
	</head>
	<body>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><sec:ifAnyGranted roles="ROLE_ADMIN"><a class="adminHome" href="${createLink(uri: '/admin')}">Admin Home</a></sec:ifAnyGranted></li>
				<li><sec:ifAnyGranted roles="ROLE_JUDGE,ROLE_ADMIN"><a class="judgeHome" href="${createLink(uri: '/judge')}">Judge Home</a></sec:ifAnyGranted></li>
				<li><sec:ifNotLoggedIn><g:link controller="login">Login</g:link></sec:ifNotLoggedIn></li>
				<li><sec:ifLoggedIn><g:link controller="logout">Logout</g:link></sec:ifLoggedIn></li>
			</ul>
		</div>
		<div id="page-body" role="main">
		<div id="DCCLogo" role="banner"><a href=""><img class="logo-img" src="${resource(dir:'images',file:'dcclogo.jpg')}" height="325" width="550" alt="Logo background original found here: http://www.vectoropenstock.com/1929-Grunge-Paint-Splatter-Vectors--Free-vector" title="Logo background original found here: http://www.vectoropenstock.com/1929-Grunge-Paint-Splatter-Vectors--Free-vector""/></a></div>
		
			<div id="pop" role="complementary"><g:link controller="submission" action="create">Enter a Submission</g:link></div>
			
			<p>
				<strong>Submission Deadline:</strong> April 26th at 11:59 P.M.
			</p>
			<p>
				<strong>Winners Announced:</strong> May 3rd
			</p>
			<p>
				Kitty ipsum dolor sit amet, adipiscing sollicitudin justo scratched sollicitudin, knock over the lamp nunc biting aliquam enim egestas iaculis. Jump catnip tail flick faucibus fluffy fur, scratched hairball tincidunt a vestibulum fluffy fur. I don't like that food non nibh I don't like that food, sniff jump mauris a hairball purr give me fish. Libero tortor quis et knock over the lamp orci turpis, sollicitudin climb the curtains litter box bibendum sleep on your keyboard. In viverra sagittis sniff et, tail flick jump on the table vehicula tail flick jump on the table aliquam attack. Shed everywhere sagittis nullam aliquam, purr egestas sunbathe faucibus mauris a accumsan sleep on your face leap.
			</p>
			<p>
				Nullam pharetra sollicitudin tortor lay down in your way stretching, tortor climb the curtains biting I don't like that food. Attack your ankles sleep on your face tincidunt a I don't like that food, elit hiss iaculis nullam eat ac tincidunt a. Et vestibulum sleep on your face sollicitudin sleep on your face eat the grass, iaculis tail flick tincidunt a quis nunc stuck in a tree. Sunbathe sleep in the sink chase the red dot ac non faucibus, stuck in a tree tempus vestibulum meow faucibus zzz. Rutrum meow chuf hiss, meow rip the couch purr rip the couch rip the couch egestas. Chuf feed me suspendisse puking suspendisse attack, feed me accumsan leap tortor.
			</p>
		</div>
	</body>
</html>