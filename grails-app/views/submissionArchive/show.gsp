<%@ page import="dcc.SubmissionArchive" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'submissionArchive.label', default: 'Past Submission')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list">List Past Submissions</g:link></li>
			</ul>
		</div>
		<div id="show-submissionArchive" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list submissionArchive">
			
				<g:if test="${submissionArchiveInstance?.category}">
				<li class="fieldcontain">
					<span id="category-label" class="property-label"><g:message code="submissionArchive.category.label" default="Category" /></span>
					
						<span class="property-value" aria-labelledby="category-label"><g:fieldValue bean="${submissionArchiveInstance}" field="category"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${submissionArchiveInstance?.title}">
				<li class="fieldcontain">
					<span id="title-label" class="property-label"><g:message code="submissionArchive.title.label" default="Title" /></span>
					
						<span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${submissionArchiveInstance}" field="title"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${submissionArchiveInstance?.author}">
				<li class="fieldcontain">
					<span id="author-label" class="property-label"><g:message code="submissionArchive.author.label" default="Author" /></span>
					
						<span class="property-value" aria-labelledby="author-label"><g:fieldValue bean="${submissionArchiveInstance}" field="author"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${submissionArchiveInstance?.link}">
				<li class="fieldcontain">
					<span id="link-label" class="property-label"><g:message code="submissionArchive.link.label" default="Link" /></span>
					
						<span class="property-value" aria-labelledby="link-label"><g:fieldValue bean="${submissionArchiveInstance}" field="link"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${submissionArchiveInstance?.steps}">
				<li class="fieldcontain">
					<span id="steps-label" class="property-label"><g:message code="submissionArchive.steps.label" default="Steps" /></span>
					
						<span class="property-value" aria-labelledby="steps-label"><g:fieldValue bean="${submissionArchiveInstance}" field="steps"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${submissionArchiveInstance?.applications}">
				<li class="fieldcontain">
					<span id="applications-label" class="property-label"><g:message code="submissionArchive.applications.label" default="Applications" /></span>
					
						<span class="property-value" aria-labelledby="applications-label"><g:fieldValue bean="${submissionArchiveInstance}" field="applications"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${submissionArchiveInstance?.goals}">
				<li class="fieldcontain">
					<span id="goals-label" class="property-label"><g:message code="submissionArchive.goals.label" default="Goals" /></span>
					
						<span class="property-value" aria-labelledby="goals-label"><g:fieldValue bean="${submissionArchiveInstance}" field="goals"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${submissionArchiveInstance?.contestYear}">
				<li class="fieldcontain">
					<span id="contestYear-label" class="property-label"><g:message code="submissionArchive.contestYear.label" default="Contest Year" /></span>
					
						<span class="property-value" aria-labelledby="contestYear-label">${submissionArchiveInstance.contestYear}</span>
					
				</li>
				</g:if>
				<g:if test="${submissionArchiveInstance?.winner}">
				<li class="fieldcontain">
					
						<span class="property-value winner" aria-labelledby="contestYear-label">Winner</span>
					
				</li>
				</g:if>
			</ol>
		</div>
	</body>
</html>