package com.apolloti.getmarry.service;

import org.granite.tide.annotations.TideEnabled;

import com.apolloti.getmarry.domain.Convidado;

@TideEnabled
class ConvidadoService
{	
	boolean transactional = true
	
	def verifyCode( Convidado convidado )
	{
		if ( convidado.getCodigo() != null && !convidado.getCodigo().equals("") )
		{
			Convidado con = Convidado.findByCodigo( convidado.getCodigo() );
		
			if ( con == null )
				throw new RuntimeException("Não foi possível localizar o convidado com o código informado.\n\nPor favor, tente novamente ou solicite um novo código.");
			
			return con;
		}
		else
			throw new RuntimeException("O código inserido está inválido.\n\nPor favor, tente novamente ou solicite um novo código.");
	}
	
	def findById( String id )
	{
		Convidado convidado = Convidado.findById( id );
		
		if ( convidado != null )
			return convidado;
		else
			throw new RuntimeException("Não foi possível localizar o convidado com a ID informada.");
	}
	
	def save( Convidado convidado )
	{
		return convidado.merge();
	}
}
