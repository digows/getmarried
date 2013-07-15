package com.apolloti.getmarry.domain

class Loja implements java.io.Serializable
{
	static mapping = 
	{
		id generator:'uuid'; 
	}
	
	String id;
	String nome;
	String endereco;
	
	public String toString() 
	{
		return nome;
	}
}