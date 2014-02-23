package dcc

import org.springframework.transaction.annotation.Transactional

class SubmissionService {

	 /*
	  * Show a list of Submissions in the database
	  */
	 def list(Integer max,params) {
		 def result = [:]
		 params.max = Math.min(max ?: 10, 100)
		 result.submissionInstanceList = Submission.list(params)
		 result.submissionInstanceTotal = Submission.count()
		 if(!result){
			 result.error = [code:"default.method.failure",args:["Could not produce a list of Submissions.",""]]
		 }
		 return result
	 }
 
	 /*
	  * Show a list of winning Submissions in the database
	  */
	 def listWinners(Integer max,params) {
		 def result = [:]
		 params.max = Math.min(max ?: 10, 100)
		 result.submissionInstanceList = Submission.findAllByWinner(true)
		 result.submissionInstanceTotal = Submission.countByWinner(true)
		 if(!result){
			 result.error = [code:"default.method.failure",args:["Could not produce a list of winning Submissions.",""]]
		 }
		 return result
	 }
 
	 /*
	  * Create a new, blank submissionInstance to create a submission.
	  */
	 @Transactional
	 def create(params) {
		 def result = [:]
		 result.submissionInstance = new Submission(params)
		 if(!result.submissionInstance){
			 result.error = [code:"default.method.failure",args:["Could not create a new Submission.",""]]
		 }
		 return result
	 }
 
	 /*
	  * Create a new submissionInstance and fill up the values from the
	  * params list which is sent over by posting the form on the create page.
	  */
	 @Transactional
	 def save(params) {
		 def result = [:]
		 result.submissionInstance = new Submission(params)
		 if(result.submissionInstance.hasErrors() || !result.submissionInstance.save(flush: true)){
			 result.error = [code:"default.method.failure",args:["Could not create new Submission",""]]
		 }
		 return result
	 }

	 /*
	  * Show the user that their submission has been created.
	  * Don't use the default show method, that is for logged in users.
	  */
	 def confirmSubmission(Long id) {
		 def result = [:]
		 result.submissionInstance = Submission.read(id)
		 if (!result.submissionInstance) {
			 result.error = [code:'default.not.found.message',args:['Submission',id]]
		 }
		 return result
	 }

	 /*
	  * Show an individual submission.
	  */
	 def show(Long id) {
		 def result = [:]
		 result.submissionInstance = Submission.read(id)
		 if (!result.submissionInstance) {
			 result.error = [code:'default.not.found.message',args:['Submission',id]]
		 }
		 return result
	 }

	 /*
	  * Show an individual winning submission.
	  */
	 def showWinner(Long id) {
		 def result = [:]
		 result.submissionInstance = Submission.read(id)
		 if (!result.submissionInstance || result.submissionInstance.winner == false) {
			 result.error = [code:'default.not.found.message',args:['Submission',id]]
		 }
		 return result
	 }

	 /*
	  * Edit an individual submission to show extra properties of the submission, edit the submission, or delete the submission
	  */
	 def edit(Long id) {
		 def result = [:]
		 result.submissionInstance = Submission.get(id)
		 if (!result.submissionInstance) {
			 result.error = [code:'default.not.found.message',args:['Submission',id]]
		 }
		 return result
	 }
 
	 /*
	  * Save any changes made to the submission from the edit method
	  */
	 @Transactional
	 def update(Long id, Long version, params) {
		 def result = [:]
		 result.submissionInstance = Submission.get(id)
		 //Not Found
		 if(!result.submissionInstance) {
			 result.error = [code:'default.not.found.message', args: ['Submission', id]]
			 return result
		 }
		 //Someone else updated it before we could
		 if(version != null) {
			 if(result.submissionInstance.version > version) {
				 result.error = [code:"default.optimistic.locking.failure",args:["Submission",""]]
				 return result
			 }
		 }
		 //Could not save
		 result.submissionInstance.properties = params
		 if(result.submissionInstance.hasErrors() || !result.submissionInstance.save(flush: true)) {
			 result.error = [code:"default.method.failure",args:["Could not update Submission",""]]
			 return result
		 }
		 //Success
		 return result
	 }
	 
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
		 result.submissionInstance = Submission.get(id)
		 if(!result.submissionInstance) {
			 result.error = [code: 'default.not.found.message', args: ['Submission', id]]
			 return result
		 }
		 result.submissionInstance.delete(flush: true)
		 return result
	 }
 }