package com.apolloti.getmarry.service;

import org.granite.tide.annotations.TideEnabled;

import com.apolloti.getmarry.domain.Convidado;
import com.apolloti.getmarry.domain.Notificacao;

@TideEnabled
class NotificacaoService
{	
	boolean transactional = true
	
	def getAll()
	{
		return Notificacao.getAll();
	}
	
	def notifyPurchase( Notificacao notificacao )
	{
		//Change the presente status
		if ( notificacao.getPresente().getQuantidade() == 0 )
		{
			notificacao.getPresente().setStatus("Comprado");
		}
		else if ( notificacao.getPresente().getQuantidade() == 1 )
		{
			notificacao.getPresente().setQuantidade( notificacao.getPresente().getQuantidade()-1 );
			notificacao.getPresente().setStatus("Comprado");
		}
		else
		{
			notificacao.getPresente().setQuantidade( notificacao.getPresente().getQuantidade()-1 );
			notificacao.getPresente().setStatus("A Comprar");
		}
		notificacao.getPresente().save();
		
		notificacao.save();
		
		//Notify by e-mail.
		String msg = "Olá Casal mais perfeito do Mundo!";
		msg += "<br/><br/>Notificamos que o (a) "+notificacao.getConvidado().getNome()+" marcou o presente "+notificacao.getPresente().getNome()+" como já comprado!";
		msg += "<br/><br/>Maiores detalhes: ";
		msg += "<br/>Nome: "+notificacao.getConvidado().getNome();
		msg += "<br/>E-mail: "+notificacao.getConvidado().getEmail();
		msg += "<br/>Telefone: "+notificacao.getConvidado().getTelefone();
		msg += "<br/>Observação: "+notificacao.getObservacao();
		msg += "<br/>-----------------------------------";
		msg += "<br/>Presente: "+notificacao.getPresente().getNome();
		msg += "<br/>Marca: "+notificacao.getPresente().getMarca().getNome();
		msg += "<br/>Modelo: "+notificacao.getPresente().getModelo();
		msg += "<br/>Loja: "+notificacao.getPresente().getLoja().getNome();
		msg += "<br/>Categoria: "+notificacao.getPresente().getCategoria().getNome();
		
		sendMail 
		{
			to "rpffoz@gmail.com", "elizfoz@hotmail.com" 
			from "no-reply@digows.com"
			subject "Notificação de Compra de Presente."
			html msg
		}
	}
	
	def notifyCodeRequest( Convidado convidado )
	{
		convidado.generateCode();
		convidado.setStatus("Bloqueado");
		convidado.save(flush:true);
		
		//Notify by e-mail.
		String msg = "Olá Casal mais perfeito do Mundo!";
		msg += "<br/><br/>Notificamos que o (a) <b>"+convidado.getNome()+"</b> está solicitando um código para acessar a lista de presentes.";
		msg += "<br/><br/>Maiores detalhes: ";
		msg += "<br/>Nome: "+convidado.getNome();
		msg += "<br/>E-mail: "+convidado.getEmail();
		msg += "<br/>Telefone: "+convidado.getTelefone();
		
		if ( convidado.getObservacao() != null )
			msg += "<br/>Observação: "+convidado.getObservacao();
		
		msg += "<br/>-----------------------------------";
		msg += "<br/>Para autorizar esta pessoa, basta acessar o link: ";
		msg += "<a href='http://www.digows.com/casamento/getmarry.html?id="+ convidado.getId() +"'>Autorizar</a><br/><br/>";

		sendMail 
		{     
			to "rpffoz@gmail.com", "elizfoz@hotmail.com" 
			from "no-reply@digows.com"
			subject "Notificação de Solicitação de Código."     
			html msg
		}
	}
	
	def notifyRequestApproved( Convidado convidado )
	{
		convidado.setStatus("Autorizado");
		convidado.merge();
		
		//Notify by e-mail.
		String msg = "Olá "+convidado.getNome()+"!";
		msg += "<br/><br/>Notificamos que sua solicatação para acesso a lista de presentes dos noivos foi aprovada com sucesso!";
		msg += "<br/>Para acessar a Lista de presentes basta abrir clicando " +
				"<a href='http://www.digows.com/casamento/lista-de-presentes/'>AQUI</a> e inserir o código: <b>"+convidado.getCodigo()+"</b>";
		msg += "<br/>";

		sendMail
		{     
			to convidado.getEmail()
			from "no-reply@digows.com"
			subject "Notificação de Autorização de Acesso."     
			html msg
		}
	}
}