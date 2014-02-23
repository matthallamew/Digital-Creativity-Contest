<%@ page import="dcc.SubmissionArchive" %>
<div class="required">
	<label for="category">
		<g:message code="submissionArchive.contestYear.label" default="Contest year to archive" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="contestYear" required="true" value=""/>
	<p>(This will be associated with each submission that is in the live area.)</p>
</div>
