<!doctype html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<g:set var="entityName" value="${message(code: 'submissionArchive.label', default: 'Submission')}" />
		<title>Submission Archive Page</title>
	</head>
	<body>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><sec:ifAnyGranted roles="ROLE_ADMIN"><a class="adminHome" href="${createLink(uri: '/admin')}">Admin Home</a></sec:ifAnyGranted></li>
				<sec:ifNotLoggedIn><li><g:link controller="login">Login</g:link></li></sec:ifNotLoggedIn>
				<sec:ifLoggedIn><li><g:link controller="logout">Logout</g:link></li></sec:ifLoggedIn>
				</li>
			</ul>
		</div>

	  <div class="content">
	  	<h1>Submission Archive</h1>
	  	<br />
		<g:if test="${flash.message}">
		<div class="message" role="message">${flash.message}</div>
		</g:if>
		<p>
			Here you may view the submissions in the archive.  You may move the current submissions to the archive to get ready for a new contest.
		</p>
		<br />
  		<ul>
	  		<li><input class="button-normal" type="button" onclick="location.href='/dcc/submissionArchive/list';" value="List Archive Contents" /></li>
	  		<li><input class="button-normal" type="button" onclick="location.href='/dcc/submissionArchive/archiveSubmissions';" value="Archive Current Submissions" /></li>
  		</ul>
	  </div>
	</body>
</html>