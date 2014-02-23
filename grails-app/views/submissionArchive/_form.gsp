<%@ page import="dcc.SubmissionArchive" %>
<div class="fieldcontain ${hasErrors(bean: submissionArchiveInstance, field: 'category', 'error')} required">
	<label for="category">
		<g:message code="submissionArchive.category.label" default="Category" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="category" required="" value="${submissionArchiveInstance?.category}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: submissionArchiveInstance, field: 'title', 'error')} required">
	<label for="title">
		<g:message code="submissionArchive.title.label" default="Title" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="title" required="" value="${submissionArchiveInstance?.title}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: submissionArchiveInstance, field: 'author', 'error')} ">
	<label for="author">
		<g:message code="submissionArchive.author.label" default="Author" />
		
	</label>
	<g:textField name="author" value="${submissionArchiveInstance?.author}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: submissionArchiveInstance, field: 'emailAddr', 'error')} ">
	<label for="emailAddr">
		<g:message code="submissionArchive.emailAddr.label" default="Email Addr" />
		
	</label>
	<g:field type="email" name="emailAddr" value="${submissionArchiveInstance?.emailAddr}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: submissionArchiveInstance, field: 'link', 'error')} required">
	<label for="link">
		<g:message code="submissionArchive.link.label" default="Link" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="link" required="" value="${submissionArchiveInstance?.link}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: submissionArchiveInstance, field: 'steps', 'error')} ">
	<label for="steps">
		<g:message code="submissionArchive.steps.label" default="Steps" />
		
	</label>
	<g:textArea name="steps" cols="40" rows="5" maxlength="1000" value="${submissionArchiveInstance?.steps}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: submissionArchiveInstance, field: 'applications', 'error')} ">
	<label for="applications">
		<g:message code="submissionArchive.applications.label" default="Applications" />
		
	</label>
	<g:textArea name="applications" cols="40" rows="5" maxlength="1000" value="${submissionArchiveInstance?.applications}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: submissionArchiveInstance, field: 'goals', 'error')} ">
	<label for="goals">
		<g:message code="submissionArchive.goals.label" default="Goals" />
		
	</label>
	<g:textArea name="goals" cols="40" rows="5" maxlength="1000" value="${submissionArchiveInstance?.goals}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: submissionArchiveInstance, field: 'contestYear', 'error')} required">
	<label for="contestYear">
		<g:message code="submissionArchive.contestYear.label" default="Contest Year" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="contestYear" type="number" value="${submissionArchiveInstance.contestYear}" required=""/>
</div>