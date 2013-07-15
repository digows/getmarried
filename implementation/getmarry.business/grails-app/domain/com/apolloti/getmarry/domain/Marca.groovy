package com.apolloti.getmarry.domain

class Marca implements java.io.Serializable
{
	static mapping = 
	{
		id generator:'uuid'; 
	}

    static constraints = 
	{
		descricao(widget:"textArea",nullable:true, blank:true)
	}
	
	String id;
	String nome;
	String descricao;
	
	public String toString() 
	{
		return nome;
	}
}
