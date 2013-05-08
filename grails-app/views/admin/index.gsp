<!doctype html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<g:set var="entityName" value="${message(code: 'judge.label', default: 'Admin')}" />
		<title>Administration Page</title>
	</head>
	<body>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<sec:ifNotLoggedIn><li><g:link controller="login">Login</g:link></li></sec:ifNotLoggedIn>
				<sec:ifLoggedIn><li><g:link controller="logout">Logout</g:link></li></sec:ifLoggedIn>
				</li>
			</ul>
		</div>

	  <div class="content">
	  	<h1>Administration Page</h1>
	  	<br />
	  	<p>${submissionsText} in the database</p>
	  	<br /><br />
  		<ul>
	  		<li><input type="button" onclick="location.href='/dcc/admin/list';" value="Show Curent Admins" /></li>
	  		<li><input type="button" onclick="location.href='/dcc/judge/list';" value="Show Curent Judges" /></li>
	  		<li><input type="button" onclick="location.href='/dcc/submission/list';" value="Show Submissions" /></li>
	  		<li><input type="button" onclick="location.href='/dcc/admin/showScoring';" value="Show Scoring" /></li>
  		</ul>
	  </div>
	</body>
</html>