package com.apolloti.getmarry.domain

class Presente implements java.io.Serializable
{
	static mapping = 
	{
		id generator:'uuid';
		columns 
		{
			marca lazy:false;
			categoria lazy:false;
			loja lazy:false;
		}
	}
	static belongsTo = [marca:Marca, categoria:Categoria, loja:Loja]

    static constraints = 
	{
		status(inList:["A Comprar","Comprado"])
		descricao(widget:"textArea",nullable:true, blank:true)
	}

    String id;
	String codigo;
	String nome;
	Integer quantidade = 1;
	String descricao;
	String modelo;
	
	Marca marca;
	Categoria categoria;
	Loja loja;
	String status = "A Comprar";
	
	public String toString() 
	{
		return codigo+" - "+nome;
	}
}