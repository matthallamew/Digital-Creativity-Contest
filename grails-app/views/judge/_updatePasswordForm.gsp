<%@ page import="dcc.Judge" %>
<div class="fieldcontain ${hasErrors(bean: judgeInstance, field: 'username', 'error')} required">
	<label for="firstName">
		<g:message code="judge.username.label" default="Username" />
	</label>
	<span><g:fieldValue bean="${judgeInstance}" field="username"/></span>
</div>

<div class="fieldcontain ${hasErrors(bean: judgeInstance, field: 'firstName', 'error')} required">
	<label for="firstName">
		<g:message code="judge.firstName.label" default="First Name" />
	</label>
	<span><g:fieldValue bean="${judgeInstance}" field="firstName"/></span>
</div>
<div class="fieldcontain ${hasErrors(bean: judgeInstance, field: 'lastName', 'error')} required">
	<label for="lastName">
		<g:message code="judge.lastName.label" default="Last Name" />
	</label>
	<span><g:fieldValue bean="${judgeInstance}" field="lastName"/></span>
</div>

<div class="fieldcontain ${hasErrors(bean: judgeInstance, field: 'password', 'error')} required">
	<label for="password">
		<g:message code="judge.password.label" default="Password" />
		<span class="required-indicator">*</span>
	</label>
	<g:passwordField name="password" required="" value=""/>
</div>