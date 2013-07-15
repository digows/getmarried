package com.apolloti.getmarry.service;

import org.granite.tide.annotations.TideEnabled;

import com.apolloti.getmarry.domain.Categoria;

@TideEnabled
class CategoriaService
{	
	boolean transactional = true
	
	def getAll()
	{
		return Categoria.list()
	}
}
