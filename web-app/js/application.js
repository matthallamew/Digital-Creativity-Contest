if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});
	})(jQuery);
}
var showSubmission = function(url){
	var myWindow = window.open (url,"Submission","scrollbars=1,resizable=1,width=920,height=840");
	var width = window.innerWidth / 2
	myWindow.moveTo(width,0);
};
var updateTotal = function(){
	var total = parseInt(document.rankForm.skill.value) + parseInt(document.rankForm.creativity.value) + parseInt(document.rankForm.aesthetic.value) + parseInt(document.rankForm.purpose.value);
	document.rankForm.total.value = total;
};
