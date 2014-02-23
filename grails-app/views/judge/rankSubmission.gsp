<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'submission.label', default: 'Submission')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><sec:ifAnyGranted roles="ROLE_JUDGE,ROLE_ADMIN"><a class="judgeHome" href="${createLink(uri: '/judge')}">Judge Home</a></sec:ifAnyGranted></li>
				<li><g:link controller="judge" class="listSubmission" action="unrankedSubmissions">Rank Submissions</g:link></li>
				<li><sec:ifNotLoggedIn><g:link controller="login">Login</g:link></sec:ifNotLoggedIn></li>
				<li><sec:ifLoggedIn><g:link controller="logout">Logout</g:link></sec:ifLoggedIn></li>
			</ul>
		</div>
		<div id="show-submission" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${rankInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${rankInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:hasErrors bean="${submissionInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${submissionInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>

			<ol class="property-list submission">
			
				<li class="fieldcontain">
					<span id="title-label" class="property-label"><g:message code="submission.title.label" default="Title" /></span>
					
						<span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${submissionInstance}" field="title"/></span>
					
				</li>
			
				<li class="fieldcontain">
					<span id="author-label" class="property-label"><g:message code="submission.author.label" default="Author" /></span>
					
						<span class="property-value" aria-labelledby="author-label"><g:fieldValue bean="${submissionInstance}" field="author"/></span>
					
				</li>
			
				<li class="fieldcontain">
					<span id="link-label" class="property-label"><g:message code="submission.link.label" default="Link" /></span>
					
					<span class="property-value" aria-labelledby="link-label"><a href="" onclick="showSubmission('${submissionInstance.link}');"><g:fieldValue bean="${submissionInstance}" field="link"/></a></span>
				</li>

				<li class="fieldcontain">
					<span id="steps-label" class="property-label"><g:message code="submission.steps.label" default="Steps" /></span>
					
						<span class="property-value" aria-labelledby="steps-label"><g:fieldValue bean="${submissionInstance}" field="steps"/></span>
					
				</li>
			
				<li class="fieldcontain">
					<span id="applications-label" class="property-label"><g:message code="submission.applications.label" default="Applications" /></span>
					
						<span class="property-value" aria-labelledby="applications-label"><g:fieldValue bean="${submissionInstance}" field="applications"/></span>
					
				</li>
			
				<li class="fieldcontain">
					<span id="goals-label" class="property-label"><g:message code="submission.goals.label" default="Goals" /></span>
					
						<span class="property-value" aria-labelledby="goals-label"><g:fieldValue bean="${submissionInstance}" field="goals"/></span>
					
				</li>			
			</ol>
			
			<g:form name="rankForm" controller="judge" action="saveRank" onsubmit="updateTotal();">
				<fieldset class="form">
					<g:render template="rankForm"/>
				</fieldset>
				<g:hiddenField name="submissionId" id="submissionId" value="${submissionInstance.id}"/>
				<g:hiddenField name="total" value=""/>
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="Submit Rank" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>