<!doctype html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<g:set var="entityName" value="${message(code: 'submissionArchive.label', default: 'SubmissionArchive')}" />
		<title>Archive Submissions</title>
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
	  	<h1>Archive Current Submissions</h1>
	  	<br />
		<g:if test="${flash.message}">
		<div class="errors" role="errors">${flash.message}</div>
		</g:if>	  	
	  	<p>${submissionsText} in the database to archive.</p>
	  	<p>
	  		<strong>Remember</strong>, archiving submissions <strong>removes</strong> them from the live area.  It also removes any ranks associated with them.
	  	</p>
		<g:form action="moveSubmissions" >
			<fieldset class="form">
				<g:render template="archiveForm"/>
			</fieldset>
			<fieldset class="buttons">
				<g:submitButton name="moveSubmissions" class="moveSubmissions" value="${message(code: 'default.button.moveSubmissions.label', default: 'Archive Submissions')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
			</fieldset>
		</g:form>
	  </div>
	</body>
</html>