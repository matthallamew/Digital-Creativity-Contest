<div class="fieldcontain ${hasErrors(bean: submissionInstance, field: 'category', 'error')} required">
	<label for="Category">
		<g:message code="submission.category.label" default="Categroy:" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="category" name="category" from="${submissionCategories.configValue}" noSelection="${['':'Select Category']}" value="${submissionInstance?.category}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: submissionInstance, field: 'title', 'error')} required">
	<label for="title">
		<g:message code="submission.title.label" default="Title:" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="title" required="" value="${submissionInstance?.title}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: submissionInstance, field: 'author', 'error')} required">
	<label for="author">
		<g:message code="submission.author.label" default="Author:" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="author" required="" value="${submissionInstance?.author}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: submissionInstance, field: 'emailAddr', 'error')} required">
	<label for="emailAddr">
		<g:message code="submission.emailAddr.label" default="Email:" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="emailAddr" required="" value="${submissionInstance?.emailAddr}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: submissionInstance, field: 'link', 'error')} required">
	<label for="link">
		<g:message code="submission.link.label" default="Link:" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="link" required="" value="${submissionInstance?.link}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: submissionInstance, field: 'steps', 'error')} ">
	<label for="steps">
		<!--<g:message code="submission.steps.label" default="Steps" />-->
		Steps Taken:
	</label>
	<g:textArea name="steps" maxlength="1000" value="${submissionInstance?.steps}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: submissionInstance, field: 'applications', 'error')} ">
	<label for="applications">
		<!--<g:message code="submission.applications.label" default="Applications" />-->
		Applications/Software Used:		
	</label>
	<g:textArea name="applications" maxlength="1000" value="${submissionInstance?.applications}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: submissionInstance, field: 'goals', 'error')} ">
	<label for="goals">
		<g:message code="submission.goals.label" default="Goals:" />
	</label>
	<g:textArea name="goals" maxlength="1000" value="${submissionInstance?.goals}"/>
</div>