package com.apolloti.getmarry.controller
{
	import com.apolloti.getmarry.domain.Categoria;
	import com.apolloti.getmarry.domain.Notificacao;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	
	import org.granite.tide.events.TideResultEvent;

	[Name("presenteSelectorController")]
	public class PresenteSelectorController
	{
		/*-------------------------------------------------------------------
		* 		 					ATTRIBUTES
		*-------------------------------------------------------------------*/
		//Model
		private var _categorias:ArrayCollection;
		
		[In] 
		[Out]
		[Bindable]
		public var presentes:ArrayCollection;
		
		public var currentFilter:String;
		public var currentCategoria:Categoria;
		
		//Controller
		[In]
		public var getMarryController:GetMarryController;
		
		//Services
		[In]
		public var presenteService:Object;
		[In]
		public var notificacaoService:Object;
		[In]
		public var categoriaService:Object;
		
		/*-------------------------------------------------------------------
		* 		 					CONSTRUCTOR
		*-------------------------------------------------------------------*/
		public function PresenteSelectorController()
		{
		}
		
		/*-------------------------------------------------------------------
		* 		 					BEHAVIORS
		*-------------------------------------------------------------------*/
		public function initialize():void
		{
			this.fillCategorias();
			this.fillProdutosByCategoria();
		}
			
		public function fillCategorias():void
		{
			this.categoriaService.getAll( getAllResult );
			
			function getAllResult( event:TideResultEvent ):void
			{
				categorias = ArrayCollection(event.result);	
			}
		}
				
		public function fillProdutosByCategoria( categoria:Categoria=null, filter:String=null ):void
		{
			this.currentCategoria = categoria;
			this.currentFilter = filter;
			
			presenteService.findAllByCategoria( categoria, filter, getAllResult );
			
			function getAllResult( event:TideResultEvent ):void
			{
				presentes = ArrayCollection(event.result);	
				getMarryController.currentView = GetMarryController.PRESENTE_SELECTOR_VIEW;
			}
		}
		
		public function notifyPurchase( notificacao:Notificacao ):void
		{
			notificacao.convidado = getMarryController.convidado;
			
			if ( notificacao.presente.status == "Comprado" )
			{
				Alert.show("Este presente já foi comprado por outro convidado. \nTem certeza que deseja marca este presente como comprado também?","Atenção!", Alert.YES | Alert.NO, null, closeHandler);
				
				function closeHandler( event:CloseEvent ):void
				{
					if ( event.detail == Alert.YES )
					{
						notificacaoService.notifyPurchase( notificacao, notifyPurchaseResult );
					}
				}
			}
			else
				notificacaoService.notifyPurchase( notificacao, notifyPurchaseResult );
			
			function notifyPurchaseResult( event:TideResultEvent ):void
			{
				Alert.show("Muitíssimo Obrigado!!!\nOs noivos foram notificados com sucesso!", "Notificação de Compra.");
				fillProdutosByCategoria(currentCategoria, currentFilter);
			}
		}
		
		/*-------------------------------------------------------------------
		* 		 					GETTERS AND SETTERS
		*-------------------------------------------------------------------*/
		[Bindable]
		public function get categorias():ArrayCollection
		{
			return this._categorias;
		}
		[In] 
		[Out]
		public function set categorias( value:ArrayCollection ):void
		{
			this._categorias = value;
			
			if ( _categorias )
				this._categorias.addItemAt(new Categoria("Todos"), 0 );
		}
	}
}