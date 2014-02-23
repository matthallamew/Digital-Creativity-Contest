package dcc

import org.springframework.transaction.annotation.Transactional

class SubmissionArchiveService {

	 /*
	  * Show a list of Submissions in the archive
	  */
	 def list(params) {
		 def result = [:]
		 params.max = params.max ?: 10
		 params.offset = params.offset ?: 0
		 result.submissionArchiveInstanceList = SubmissionArchive.withCriteria{ if(!params.sort){order "contestYear","desc"; order "category","asc";}; else{order "$params.sort","$params.order";}; firstResult params.offset.toInteger(); maxResults params.max.toInteger();}
		 result.submissionArchiveInstanceTotal = SubmissionArchive.count()
		 if(!result.submissionArchiveInstanceList || !result.submissionArchiveInstanceTotal){
			 result.error = [code:"default.method.failure",args:["Could not produce a list of Submissions.",""]]
		 }
		 return result
	 }

	 /*
	  * Show a list of past winning Submissions in the database
	  */
	 def listWinners(Integer max,Integer offset,params) {
		 def result = [:]
		 params.max = Math.min(max ?: 10, 100)
		 params.offset = Math.min(offset ?: 0, 100)
		 result.submissionArchiveInstanceList = SubmissionArchive.withCriteria{ eq "winner",true; if(!params.sort){ order "contestYear","desc"; order "category","asc";}; else{order "$params.sort","$params.order";}; firstResult params.offset.toInteger(); maxResults params.max.toInteger();}
		 result.submissionArchiveInstanceTotal = SubmissionArchive.countByWinner(true)
		 if(!result.submissionArchiveInstanceList || !result.submissionArchiveInstanceTotal){
			 result.error = [code:"default.method.failure",args:["Could not produce a list of past winning Submissions.",""]]
		 }
		 return result
	 }
 
	 /*
	  * Get a count of the submissions in the live table (Submission).
	  * Ask the user for a year that will get associated with all the Submissions being moved to the archive.
	  */
	 def archiveSubmissions() {
		 def result = [:]
		 def submissionCount = Submission.count()
		 if(!submissionCount) {
			 result.error = [code:"default.method.failure",args:["There are no Submissions to archive.",""]]
			 return result
		 }
		 def subText = (submissionCount <= 0) ? "Submissions are " : (submissionCount > 1) ? "Submissions are " : "Submission is "
		 result.submissionsText = "$submissionCount $subText"
		 if(!result.submissionsText){
			 result.error = [code:"default.method.failure",args:["Could not count the current submissions.",""]]
			 return result
		 }
		 return result
	 }
 
	 /*
	  * Get a list of all the submissions in the live table (Submission).
	  * Update each records grandTotal just to make sure it has the most up to date value.
	  * For each record, create a new submissionArchive and fill it up with the values from the Submission record.
	  * Add the year the user inputs in the archiveSubmission method to the submissionArchiveInstance.
	  * If the save is successful, delete the Submission record.
	  * The delete is cascaded to any ranks associated with the Submission record.
	  */
	 @Transactional
	 def moveSubmissions(params) {
		 def contestYear = params.contestYear.toInteger()
		 def result = [:]
		 //get all the submissions
		 def submissions = Submission.list()
		 if(!submissions){
			 result.error = [code:"default.method.failure",args:["Could not find any submissions.",""]]
			 return result
		 }
		 submissions.each{ sub ->
		//Update grand total of each submission to get most current grand total
			 if(!sub.updateGrandTotal()) {
				 throw new Exception()
			 }
		//Save them to archive with year
			 def submissionArchiveInstance = new SubmissionArchive()
			 submissionArchiveInstance.properties = sub.properties
			 submissionArchiveInstance.contestYear = contestYear
			 if(submissionArchiveInstance.hasErrors() || !submissionArchiveInstance.save()){
				 throw new Exception()
			 }
			 sub.delete()
		 }
		 result.success = true
		 return result
	 }
	 
	 /*
	  * Permanently delete ALL submissions from the archives.
	  */
	 @Transactional
	 def clearArchive(){
		 def results = [:]
		 results.deleteCount = 0
		 if(SubmissionArchive.count() > 0) {
			 results.deleteCount = SubmissionArchive.executeUpdate("DELETE FROM SubmissionArchive")
			 return results
		 }
		 result.error = [code:"default.method.failure",args:["Could not find any submissions to remove.",""]]
		 return results
	 }

	 /* UNUSED METHOD:
	  *  Create a new, blank submissionArchiveInstance to create a submission.
	  */
//	 @Transactional
//	 def create(params) {
//		 def result = [:]
//		 result.submissionArchiveInstance = new SubmissionArchive(params)
//		 if(!result.submissionArchiveInstance){
//			 result.error = [code:"default.method.failure",args:["Could not create a new SubmissionArchive.",""]]
//		 }
//		 return result
//	 }
 
	 /* UNUSED METHOD:
	  * Create a new submissionArchiveInstance and fill up the values from the
	  * params list which is sent over by posting the form on the create page.
	  */
//	 @Transactional
//	 def save(params) {
//		 def result = [:]
//		 result.submissionArchiveInstance = new SubmissionArchive(params)
//		 if(result.submissionArchiveInstance.hasErrors() || !result.submissionArchiveInstance.save(flush: true)){
//			 result.error = [code:"default.method.failure",args:["Could not create new SubmissionArchive",""]]
//		 }
//		 return result
//	 }

	 /*
	  * Show an individual submission.
	  */
	 def show(Long id) {
		 def result = [:]
		 result.submissionArchiveInstance = SubmissionArchive.get(id)
		 if (!result.submissionArchiveInstance) {
			 result.error = [code:'default.not.found.message',args:['SubmissionArchive',id]]
		 }
		 return result
	 }
 
	 /*
	  * Show an individual winning submission.
	  */
	 def showWinner(Long id) {
		 def result = [:]
		 result.submissionArchiveInstance = SubmissionArchive.read(id)
		 if (!result.submissionArchiveInstance) {
			 result.error = [code:'default.not.found.message',args:['SubmissionArchive',id]]
		 }
		 return result
	 }

	 /* UNUSED METHOD:
	  * Edit an individual submission to show extra properties of the submission, edit the submission, or delete the submission
	  */
//	 def edit(Long id) {
//		 def result = [:]
//		 result.submissionArchiveInstance = SubmissionArchive.get(id)
//		 if (!result.submissionArchiveInstance) {
//			 result.error = [code:'default.not.found.message',args:['SubmissionArchive',id]]
//		 }
//		 return result
//	 }
 
	 /* UNUSED METHOD:
	  * Save any changes made to the submission from the edit method
	  */
//	 @Transactional
//	 def update(Long id, Long version, params) {
//		 def result = [:]
//		 result.submissionArchiveInstance = SubmissionArchive.get(id)
//		 //Not Found
//		 if(!result.submissionArchiveInstance) {
//			 result.error = [code:'default.not.found.message', args: ['SubmissionArchive', id]]
//			 return result
//		 }
//		 //Someone else updated it before we could
//		 if(version != null) {
//			 if(result.submissionArchiveInstance.version > version) {
//				 result.error = [code:"default.optimistic.locking.failure",args:["SubmissionArchive",""]]
//				 return result
//			 }
//		 }
//		 //Could not save
//		 result.submissionArchiveInstance.properties = params
//		 if(result.submissionArchiveInstance.hasErrors() || !result.submissionArchiveInstance.save(flush: true)) {
//			 result.error = [code:"default.method.failure",args:["Could not update SubmissionArchive",""]]
//			 return result
//		 }
//		 //Success
//		 return result
//	 }
	 
	 /*
	  * Try to find the submission.
	  * If it is not found, return an error.
	  * If it is found, try to delete it.
	  * If it cannot be deleted, return an error.
	  * If all goes well, return the good news.
	  */
	 @Transactional
	 def delete(Long id) {
		 def result = [:]
		 result.submissionArchiveInstance = SubmissionArchive.get(id)
		 if(!result.submissionArchiveInstance) {
			 result.error = [code: 'default.not.found.message', args: ['SubmissionArchive', id]]
			 return result
		 }
		 result.submissionArchiveInstance.delete(flush: true)
		 return result
	 }
}