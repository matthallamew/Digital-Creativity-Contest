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
				<li><a class="home" href="${createLink(uri: '/admin')}"><g:message code="default.home.label"/></a></li>
				<li><sec:ifAnyGranted roles="ROLE_ADMIN"><a class="adminHome" href="${createLink(uri: '/admin')}">Admin Home</a></sec:ifAnyGranted></li>
				<li><sec:ifAnyGranted roles="ROLE_ADMIN"><g:link action="showScoring">Show Scoring</g:link></sec:ifAnyGranted></li>
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

				<sec:ifAnyGranted roles="ROLE_ADMIN">
				<g:if test="${submissionInstance?.emailAddr}">
				<li class="fieldcontain">
					<span id="emailAddr-label" class="property-label"><g:message code="submission.emailAddr.label" default="Email" /></span>
					
						<span class="property-value" aria-labelledby="emailAddr-label"><g:fieldValue bean="${submissionInstance}" field="emailAddr"/></span>
					
				</li>
				</g:if>
				</sec:ifAnyGranted>

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
				<g:if test="${submissionInstance?.grandTotal}">
				<li class="fieldcontain">
					<span id="goals-label" class="property-label"><g:message code="submission.grandTotal.label" default="Total Score" /></span>
					
						<span class="property-value" aria-labelledby="goals-label"><g:fieldValue bean="${submissionInstance}" field="grandTotal"/></span>
					
				</li>
				</g:if>
				<g:if test="${submissionInstance?.ranks}">
				<li class="fieldcontain">
					<span id="submissions-label" class="property-label"><g:message code="submissions.ranks.label" default="Judges" /></span>
					
						<g:each in="${submissionInstance.ranks}" var="r">
							<%--<span class="property-value" aria-labelledby="ranks-label"><g:link controller="rank" action="show" id="${r.id}">${r?.encodeAsHTML()}</g:link></span>--%>
							<span class="property-value" aria-labelledby="submissions-label"><g:link controller="rank" action="show" id="${r.id}">${r.judges.firstName} ${r.judges.lastName}</g:link></span>
							<span class="property-value" aria-labelledby="submissions-label">Total Score: ${r.total}</span>
							<br />
						</g:each>
				</li>
				</g:if>
				<g:else>
				<li class="fieldcontain">
					<span id="submissions-label" class="property-label"><g:message code="submissions.ranks.label" default="Judges" /></span>
					<span class="property-value" aria-labelledby="submissions-label">No judges have ranked this Submission yet.</span>
				</li>
				</g:else>
			</ol>
  			<g:form>
				<fieldset class="form">
					<div class="fieldcontain ${hasErrors(bean: submissionInstance, field: 'steps', 'error')} ">
						<label for="steps">
							<g:message code="submission.winner.label" default="Mark submission as a Winner" />
						</label>
						<g:checkBox name="winner" value="${submissionInstance.winner}"/>
					</div>
				</fieldset>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${submissionInstance?.id}" />
					<g:hiddenField name="version" value="${submissionInstance?.version}" />
					<g:actionSubmit class="edit" action="updateWinnerStatus" id="${submissionInstance?.id}" onclick="return confirm('Are you sure?');" value="Update Winner Status" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>