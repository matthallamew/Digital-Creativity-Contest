<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'submission.label', default: 'Submission')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-submission" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><sec:ifAnyGranted roles="ROLE_ADMIN"><a class="adminHome" href="${createLink(uri: '/admin')}">Admin Home</a></sec:ifAnyGranted></li>
				<li><sec:ifAnyGranted roles="ROLE_JUDGE,ROLE_ADMIN"><a class="judgeHome" href="${createLink(uri: '/judge')}">Judge Home</a></sec:ifAnyGranted></li>
				<li><g:link class="create" action="create"><g:message code="default.create.label" args="[entityName]" /></g:link></li>
				<li><sec:ifLoggedIn><g:link controller="logout">Logout</g:link></sec:ifLoggedIn></li>
			</ul>
		</div>
		<div id="show-submission" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list submission">
			
				<g:if test="${submissionInstance?.title}">
				<li class="fieldcontain">
					<span id="title-label" class="property-label"><g:message code="submission.title.label" default="Title" /></span>
					
						<span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${submissionInstance}" field="title"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${submissionInstance?.author}">
				<li class="fieldcontain">
					<span id="author-label" class="property-label"><g:message code="submission.author.label" default="Author" /></span>
					
						<span class="property-value" aria-labelledby="author-label"><g:fieldValue bean="${submissionInstance}" field="author"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${submissionInstance?.link}">
				<li class="fieldcontain">
					<span id="link-label" class="property-label"><g:message code="submission.link.label" default="Link" /></span>
					
					<span class="property-value" aria-labelledby="link-label"><a href="" onclick="showSubmission('${submissionInstance.link}');"><g:fieldValue bean="${submissionInstance}" field="link"/></a></span>
				</li>
				</g:if>

				<g:if test="${submissionInstance?.steps}">
				<li class="fieldcontain">
					<span id="steps-label" class="property-label"><g:message code="submission.steps.label" default="Steps" /></span>
					
						<span class="property-value" aria-labelledby="steps-label"><g:fieldValue bean="${submissionInstance}" field="steps"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${submissionInstance?.applications}">
				<li class="fieldcontain">
					<span id="applications-label" class="property-label"><g:message code="submission.applications.label" default="Applications" /></span>
					
						<span class="property-value" aria-labelledby="applications-label"><g:fieldValue bean="${submissionInstance}" field="applications"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${submissionInstance?.goals}">
				<li class="fieldcontain">
					<span id="goals-label" class="property-label"><g:message code="submission.goals.label" default="Goals" /></span>
					
						<span class="property-value" aria-labelledby="goals-label"><g:fieldValue bean="${submissionInstance}" field="goals"/></span>
					
				</li>
				</g:if>			
			</ol>
		</div>
	</body>
</html>
