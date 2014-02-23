<!doctype html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<g:set var="entityName" value="${message(code: 'judge.label', default: 'Judge')}" />
		<title>Judging Page</title>
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
	  	<h1>Judges Page</h1>
	  	<br />
	  	<p>${submissionsText}</p>
	  	<p>${unrankedSubmissionsText}</p>
	  	<br /><br />
  		<ul>
	  		<li><input type="button" onclick="location.href='/dcc/submission/list';" value="Browse the Submissions" /></li>
	  		<li><input type="button" onclick="location.href='/dcc/judge/unrankedSubmissions';" value="Rank the Submissions" /></li>
  		</ul>
	  	<br />
	  </div>
	</body>
</html>