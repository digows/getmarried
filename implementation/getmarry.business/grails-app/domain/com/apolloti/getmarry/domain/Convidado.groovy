package com.apolloti.getmarry.domain

import java.util.List;
import java.util.Random;

class Convidado implements java.io.Serializable
{
	static mapping = 
	{
		id generator:'uuid'; 
	}

	static constraints = 
	{
        nome(nullable:false, blank:false, unique:true)
        status(inList:["Bloqueado","Autorizado"])
        observacao(widget:"textArea",nullable:true, blank:true)
    }

	String id;
	Integer codigo;
	String nome;
	String email;
	String telefone;
	String observacao;
	String status;
	
	def generateCode()
	{
		Integer result = Convidado.executeQuery( "SELECT MAX( c.codigo ) FROM Convidado c" ).get(0);//FirstResult
		
		if ( result != null )
		{
			this.codigo = result+result/10; 
		}
		else
		{
			this.codigo = 101; //First record.
		}
	}
	
	public String toString() 
	{
		return nome;
	}
}
