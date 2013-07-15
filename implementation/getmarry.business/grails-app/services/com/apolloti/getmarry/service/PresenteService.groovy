package com.apolloti.getmarry.service;

import org.granite.tide.annotations.TideEnabled;

import com.apolloti.getmarry.domain.Categoria;
import com.apolloti.getmarry.domain.Presente;

@TideEnabled
class PresenteService
{	
	boolean transactional = true
	
	def getAll()
	{
		return Presente.list()
	}
	
	def findAllByCategoria( Categoria categoria, String filter )
	{
		//List all filtering by categoria
		if ( categoria != null && categoria.getId() != null && (filter != null && !filter.equals("Todos")) )
		{
			return Presente.findAllByCategoriaAndStatus( categoria, filter );
		}
		
		if ( categoria != null && categoria.getId() != null )
		{
			return Presente.findAllByCategoria( categoria );
		}
		
		if ( filter != null && !filter.equals("Todos") )
		{
			return Presente.findAllByStatus( filter );
		}
		
		return Presente.getAll();
	}
}