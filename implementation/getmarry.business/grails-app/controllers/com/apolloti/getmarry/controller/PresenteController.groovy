package com.apolloti.getmarry.controller

import org.granite.tide.annotations.TideEnabled;

import com.apolloti.getmarry.domain.Presente;

@TideEnabled
class PresenteController {
	
	def scaffold = Presente;
	
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ presenteInstanceList: Presente.list( params ), presenteInstanceTotal: Presente.count() ]
    }

    def show = {
        def presenteInstance = Presente.get( params.id )

        if(!presenteInstance) {
            flash.message = "Presente not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ presenteInstance : presenteInstance ] }
    }

    def delete = {
        def presenteInstance = Presente.get( params.id )
        if(presenteInstance) {
            try {
                presenteInstance.delete(flush:true)
                flash.message = "Presente ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Presente ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Presente not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def presenteInstance = Presente.get( params.id )

        if(!presenteInstance) {
            flash.message = "Presente not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ presenteInstance : presenteInstance ]
        }
    }

    def update = {
        def presenteInstance = Presente.get( params.id )
        if(presenteInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(presenteInstance.version > version) {
                    
                    presenteInstance.errors.rejectValue("version", "presente.optimistic.locking.failure", "Another user has updated this Presente while you were editing.")
                    render(view:'edit',model:[presenteInstance:presenteInstance])
                    return
                }
            }
            presenteInstance.properties = params
            if(!presenteInstance.hasErrors() && presenteInstance.save()) {
                flash.message = "Presente ${params.id} updated"
                redirect(action:show,id:presenteInstance.id)
            }
            else {
                render(view:'edit',model:[presenteInstance:presenteInstance])
            }
        }
        else {
            flash.message = "Presente not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def presenteInstance = new Presente()
        presenteInstance.properties = params
        return ['presenteInstance':presenteInstance]
    }

    def save = {
		Presente presenteInstance = new Presente(params)
		presenteInstance.setStatus("A Comprar");
		
        if(!presenteInstance.hasErrors() && presenteInstance.save()) {
            flash.message = "Presente ${presenteInstance.id} created"
            redirect(action:show,id:presenteInstance.id)
        }
        else {
            render(view:'create',model:[presenteInstance:presenteInstance])
        }
    }

    
    // Base actions for gdsflex
         
    def find = {
    	// Lookup an entity instance by id
    	
        def presenteInstance = Presente.get(params.id)

        return [presenteInstance: presenteInstance]
    }

    def remove = {
		// Remove an entity by id (delete cannot be used from Flex because it's a keyword)    
    
        def presenteInstance = Presente.get(params.id)
        if (presenteInstance)
            presenteInstance.delete()
    }

    def merge = {
    	// Merge detached entity from Flex
    	
        def presenteInstance = params.presenteInstance
        
        if (!presenteInstance.validate())
            throw new org.granite.tide.spring.SpringValidationException(presenteInstance.errors);
        
        presenteInstance = presenteInstance.merge()
        
        return [presenteInstance: presenteInstance]
    }

    def persist = {
    	// Persist a new entity received from Flex
    	
        def presenteInstance = params.presenteInstance
        
        if (!presenteInstance.validate())
            throw new org.granite.tide.spring.SpringValidationException(presenteInstance.errors);
        
        presenteInstance.save()
        
        return [presenteInstance: presenteInstance]
    }
    
    def upload = {
    	// Handle file upload from a Flex FileReference
    	// Supports byte[] or Blob mappings
    	
    	def presenteInstance = Presente.get(params.id)
    	
    	if (params[params.property]) {
    		if (java.sql.Blob.class.isAssignableFrom(Presente.metaClass.getMetaProperty(params.property).type)) {
    			presenteInstance[params.property] = org.hibernate.Hibernate.createBlob(params[params.property].getInputStream())
    		}
    		else {
    			def baos = new java.io.ByteArrayOutputStream()
    			baos << params[params.property].getInputStream()
    			presenteInstance[params.property] = baos.toByteArray()
    		}
    	}
    	else
    		presenteInstance[params.property] = null;
    	
    	presenteInstance.save(flush:true)
    	
    	// Init GraniteDS thread context (we are not in a normal AMF request)
    	def graniteConfig = org.granite.config.GraniteConfig.loadConfig(servletContext)
    	def servicesConfig = org.granite.config.flex.ServicesConfig.loadConfig(servletContext)
        def context = org.granite.messaging.webapp.HttpGraniteContext.createThreadIntance(
            graniteConfig, servicesConfig, servletContext,
            request, response
        ) 	
    	// Encode updated entity in AMF to return to Flex 
        def baos = new java.io.ByteArrayOutputStream()
    	def output = graniteConfig.newAMF3Serializer(baos)
    	output.writeObject(presenteInstance)
    	output.flush()
    	// Encode in Base64 (Flex file upload responses can only be strings) 
    	render(org.granite.util.Base64.encodeToString(baos.toByteArray(), false))
    }
    
    def download = {
    	// Handle download of a binary property
    	// Supports byte[] and Blob
    	
    	def presenteInstance = Presente.get(params.id)
    
    	if (presenteInstance[params.property] instanceof java.sql.Blob)
    		response.outputStream << presenteInstance[params.property].getBinaryStream()
    	else
    		response.outputStream << presenteInstance[params.property];
    }
}
