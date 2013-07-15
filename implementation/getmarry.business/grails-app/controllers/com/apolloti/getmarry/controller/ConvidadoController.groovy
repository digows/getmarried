

package com.apolloti.getmarry.controller

import org.granite.tide.annotations.TideEnabled;

import com.apolloti.getmarry.domain.Convidado;

@TideEnabled
class ConvidadoController {
	
	def scaffold = Convidado;
	
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ convidadoInstanceList: Convidado.list( params ), convidadoInstanceTotal: Convidado.count() ]
    }

    def show = {
        def convidadoInstance = Convidado.get( params.id )

        if(!convidadoInstance) {
            flash.message = "Convidado not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ convidadoInstance : convidadoInstance ] }
    }

    def delete = {
        def convidadoInstance = Convidado.get( params.id )
        if(convidadoInstance) {
            try {
                convidadoInstance.delete(flush:true)
                flash.message = "Convidado ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Convidado ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Convidado not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def convidadoInstance = Convidado.get( params.id )

        if(!convidadoInstance) {
            flash.message = "Convidado not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ convidadoInstance : convidadoInstance ]
        }
    }

    def update = {
        def convidadoInstance = Convidado.get( params.id )
        if(convidadoInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(convidadoInstance.version > version) {
                    
                    convidadoInstance.errors.rejectValue("version", "convidado.optimistic.locking.failure", "Another user has updated this Convidado while you were editing.")
                    render(view:'edit',model:[convidadoInstance:convidadoInstance])
                    return
                }
            }
            convidadoInstance.properties = params
            if(!convidadoInstance.hasErrors() && convidadoInstance.save()) {
                flash.message = "Convidado ${params.id} updated"
                redirect(action:show,id:convidadoInstance.id)
            }
            else {
                render(view:'edit',model:[convidadoInstance:convidadoInstance])
            }
        }
        else {
            flash.message = "Convidado not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def convidadoInstance = new Convidado()
        convidadoInstance.properties = params
        return ['convidadoInstance':convidadoInstance]
    }

    def save = {
		Convidado convidadoInstance = new Convidado(params);
		convidadoInstance.generateCode();
		convidadoInstance.setStatus("Autorizado")
		
        if(!convidadoInstance.hasErrors() && convidadoInstance.save()) {
            flash.message = "Convidado ${convidadoInstance.id} created"
            redirect(action:show,id:convidadoInstance.id)
        }
        else {
            render(view:'create',model:[convidadoInstance:convidadoInstance])
        }
    }

    
    // Base actions for gdsflex
         
    def find = {
    	// Lookup an entity instance by id
    	
        def convidadoInstance = Convidado.get(params.id)

        return [convidadoInstance: convidadoInstance]
    }

    def remove = {
		// Remove an entity by id (delete cannot be used from Flex because it's a keyword)    
    
        def convidadoInstance = Convidado.get(params.id)
        if (convidadoInstance)
            convidadoInstance.delete()
    }

    def merge = {
    	// Merge detached entity from Flex
    	
        def convidadoInstance = params.convidadoInstance
        
        if (!convidadoInstance.validate())
            throw new org.granite.tide.spring.SpringValidationException(convidadoInstance.errors);
        
        convidadoInstance = convidadoInstance.merge()
        
        return [convidadoInstance: convidadoInstance]
    }

    def persist = {
    	// Persist a new entity received from Flex
    	
        def convidadoInstance = params.convidadoInstance
        
        if (!convidadoInstance.validate())
            throw new org.granite.tide.spring.SpringValidationException(convidadoInstance.errors);
        
        convidadoInstance.save()
        
        return [convidadoInstance: convidadoInstance]
    }
    
    def upload = {
    	// Handle file upload from a Flex FileReference
    	// Supports byte[] or Blob mappings
    	
    	def convidadoInstance = Convidado.get(params.id)
    	
    	if (params[params.property]) {
    		if (java.sql.Blob.class.isAssignableFrom(Convidado.metaClass.getMetaProperty(params.property).type)) {
    			convidadoInstance[params.property] = org.hibernate.Hibernate.createBlob(params[params.property].getInputStream())
    		}
    		else {
    			def baos = new java.io.ByteArrayOutputStream()
    			baos << params[params.property].getInputStream()
    			convidadoInstance[params.property] = baos.toByteArray()
    		}
    	}
    	else
    		convidadoInstance[params.property] = null;
    	
    	convidadoInstance.save(flush:true)
    	
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
    	output.writeObject(convidadoInstance)
    	output.flush()
    	// Encode in Base64 (Flex file upload responses can only be strings) 
    	render(org.granite.util.Base64.encodeToString(baos.toByteArray(), false))
    }
    
    def download = {
    	// Handle download of a binary property
    	// Supports byte[] and Blob
    	
    	def convidadoInstance = Convidado.get(params.id)
    
    	if (convidadoInstance[params.property] instanceof java.sql.Blob)
    		response.outputStream << convidadoInstance[params.property].getBinaryStream()
    	else
    		response.outputStream << convidadoInstance[params.property];
    }
}
