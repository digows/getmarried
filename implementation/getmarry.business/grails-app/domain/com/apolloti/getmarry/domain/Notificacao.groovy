package com.apolloti.getmarry.domain

class Notificacao implements java.io.Serializable
{
	static mapping =
	{
		id generator:'uuid';
		
		columns 
		{
			presente lazy:false;
			convidado lazy:false;
		}
	}
	
	static belongsTo = [presente:Presente, convidado:Convidado]

	static constraints = 
	{
		observacao(widget:"textArea",nullable:true, blank:true)
    }

    String id;
	String observacao;
	
	Presente presente;
	Convidado convidado;
}
