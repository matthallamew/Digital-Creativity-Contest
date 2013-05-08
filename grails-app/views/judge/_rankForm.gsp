<div class="fieldcontain ${hasErrors(bean: rankInstance, field: 'skill', 'error')} required">
	<label for="skill">
		<g:message code="rank.skill.label" default="Skill" />
	</label>
	<g:select name="skill" from="${1..6}" class="range" required="" value="${3}"/>
	<br />
	<ul>
		<li>Time and Effort Required</li>
		<li>Polish and Professionalism</li>
		<li>Clean and Seamless Construction Using Software</li>
	</ul>
</div>
<div class="fieldcontain ${hasErrors(bean: rankInstance, field: 'skillComment', 'error')} ">
	<label for="skillComment">
		<g:message code="rank.skillComment.label" default="Skill Comment" />
	</label>
	<g:textArea name="skillComment" maxlength="750" value="${rankInstance?.skillComment}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: rankInstance, field: 'creativity', 'error')} required">
	<label for="creativity">
		<g:message code="rank.creativity.label" default="Creativity" />
	</label>
	<g:select name="creativity" from="${1..6}" class="range" required="" value="${3}"/>
	<br />
	<ul>
		<li>Originality of Concept</li>
		<li>Innovative uses of Software</li>
	</ul>
</div>

<div class="fieldcontain ${hasErrors(bean: rankInstance, field: 'creativityComment', 'error')} ">
	<label for="creativityComment">
		<g:message code="rank.creativityComment.label" default="Creativity Comment" />
		
	</label>
	<g:textArea name="creativityComment" maxlength="750" value="${rankInstance?.creativityComment}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: rankInstance, field: 'aesthetic', 'error')} required">
	<label for="aesthetic">
		<g:message code="rank.aesthetic.label" default="Aesthetic" />
	</label>
	<g:select name="aesthetic" from="${1..6}" class="range" required="" value="${3}"/>
	<br />
	<ul>
		<li>Beauty, Elegance, Economy</li>
	</ul>
</div>

<div class="fieldcontain ${hasErrors(bean: rankInstance, field: 'aestheticComment', 'error')} ">
	<label for="aestheticComment">
		<g:message code="rank.aestheticComment.label" default="Aesthetic Comment" />
	</label>
	<g:textArea name="aestheticComment" maxlength="750" value="${rankInstance?.aestheticComment}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: rankInstance, field: 'purpose', 'error')} required">
	<label for="purpose">
		<g:message code="rank.purpose.label" default="Purpose" />
	</label>
	<g:select name="purpose" from="${1..6}" class="range" required="" value="${3}"/>
	<ul>
		<li>Conveys a Meaning or a Message</li>
	</ul>
</div>

<div class="fieldcontain ${hasErrors(bean: rankInstance, field: 'purposeComment', 'error')} ">
	<label for="purposeComment">
		<g:message code="rank.purposeComment.label" default="Purpose Comment" />
		
	</label>
	<g:textArea name="purposeComment" maxlength="750" value="${rankInstance?.purposeComment}"/>
</div>