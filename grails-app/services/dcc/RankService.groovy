package dcc

import org.springframework.transaction.annotation.Transactional

class RankService {

	/*
	 * Method is unused
	 */
//	def list(Integer max,params) {
//		def result = [:]
//		params.max = Math.min(max ?: 10, 100)
//		result.rankInstanceList = Rank.list(params)
//		result.rankInstanceTotal = Rank.count()
//		if(!result){
//			result.error = [code:"default.method.failure",args:["Could not produce a list of Ranks.",""]]
//		}
//		return result
//	}

	/*
	 * Method is unused
	 * Create a new, blank rankInstance to create a rank.
	 */
//	@Transactional
//	def create(params) {
//		def result = [:]
//		result.rankInstance = new Rank(params)
//		if(!result.rankInstance){
//			result.error = [code:"default.method.failure",args:["Could not create a new Rank.",""]]
//		}
//		return result
//	}

	/*
	 * Method is unused
	 * Create a new rankInstance and fill up the values from the
	 * params list which is sent over by posting the form on the create page.
	 */
//	@Transactional
//	def save(params) {
//		def result = [:]
//		result.rankInstance = new Rank(params)
//		if(result.rankInstance.hasErrors() || !result.rankInstance.save(flush: true)){
//			result.error = [code:"default.method.failure",args:["Could not create new Rank",""]]
//		}
//		return result
//	}

	/*
	 * Show an individual rank.
	 */
	def show(Long id) {
		def result = [:]
		result.rankInstance = Rank.get(id)
		if (!result.rankInstance) {
			result.error = [code:'default.not.found.message',args:['Rank',id]]
		}
		return result
	}

	/*
	 * Method is unused
	 * Edit an individual rank to show extra properties of the rank, edit the rank, or delete the rank
	 */
//	def edit(Long id) {
//		def result = [:]
//		result.rankInstance = Rank.get(id)
//		if (!result.rankInstance) {
//			result.error = [code:'default.not.found.message',args:['Rank',id]]
//		}
//		return result
//	}

	/*
	 * Method is unused
	 * Save any changes made to the rank from the edit method
	 */
//	@Transactional
//	def update(Long id, Long version, params) {
//		def result = [:]
//		result.rankInstance = Rank.get(id)
//		//Not Found
//		if(!result.rankInstance) {
//			result.error = [code:'default.not.found.message', args: ['Rank', id]]
//			return result
//		}
//		//Someone else updated it before we could
//		if(version != null) {
//			if(result.rankInstance.version > version) {
//				result.error = [code:"default.optimistic.locking.failure",args:["Rank",""]]
//				return result
//			}
//		}
//		//Could not save
//		result.rankInstance.properties = params
//		if(result.rankInstance.hasErrors() || !result.rankInstance.save(flush: true)) {
//			result.error = [code:"default.method.failure",args:["Could not update Rank",""]]
//			return result
//		}
//		//Success
//		return result
//	}
	
	/*
	 * Method is unused
	 * Try to find the rank.
	 * If it is not found, return an error.
	 * If it is found, try to delete it.
	 * If it cannot be deleted, return an error.
	 * If all goes well, return the good news.
	 */
//	@Transactional
//	def delete(Long id) {
//		def result = [:]
//		result.rankInstance = Rank.get(id)
//		if(!result.rankInstance){
//			result.error = [code: 'default.not.found.message', args: ['Rank', id]]
//			return result
//		}
//		result.rankInstance.delete(flush: true)
//		return result
//	}	
}