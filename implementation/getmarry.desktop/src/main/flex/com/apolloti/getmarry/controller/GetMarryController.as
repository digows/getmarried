package com.apolloti.getmarry.controller
{
	import com.apolloti.getmarry.domain.Convidado;
	import com.apolloti.getmarry.util.QueryString;
	
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	
	import org.granite.tide.events.TideResultEvent;

	[Name("getMarryController")]
	public class GetMarryController
	{
		/*-------------------------------------------------------------------
		* 		 					CONSTANTS
		*-------------------------------------------------------------------*/
		public static const LOGIN_VIEW:uint = 0;
		public static const PRESENTE_SELECTOR_VIEW:uint = 1;
		
		/*-------------------------------------------------------------------
		* 		 					ATTRIBUTES
		*-------------------------------------------------------------------*/
		//Model
		[In] 
		[Out]
		[Bindable]
		public var convidado:Convidado;
		
		[In]
		[Out]
		[Bindable]
		public var currentView:uint = LOGIN_VIEW;
		
		//Controller
		[In]
		public var loginController:LoginController;
		
		
		//Services
		[In]
		public var convidadoService:Object;
		[In]
		public var notificacaoService:Object;
		
		/*-------------------------------------------------------------------
		* 		 					CONSTRUCTOR
		*-------------------------------------------------------------------*/
		public function GetMarryController()
		{
		}
		
		/*-------------------------------------------------------------------
		* 		 					BEHAVIORS
		*-------------------------------------------------------------------*/
		public function initialize():void
		{
			var convidadoID:String = QueryString.parameters.id;
			
			if ( convidadoID != null && convidadoID != "" )
			{
				convidadoService.findById( convidadoID, findByIdResult );
			}
			
			function findByIdResult( event:TideResultEvent ):void
			{
				Alert.show( "Tem certeza que deseja liberar o acesso a lista de presentes para o(a) "+Convidado(event.result).nome+"?",
					"Por favor confirme", Alert.YES | Alert.NO, null, closeHandler);
				
				function closeHandler( e:CloseEvent ):void
				{
					if ( e.detail == Alert.YES )
					{
						notificacaoService.notifyRequestApproved( Convidado(event.result), notifyRequestApprovedResult );
					}
				}
				
				function notifyRequestApprovedResult( event:TideResultEvent ):void
				{
					Alert.show("Autorização cedida com sucesso!", "Autorização de acesso.");
				}
			}
		}
			
		/*-------------------------------------------------------------------
		* 		 					GETTERS AND SETTERS
		*-------------------------------------------------------------------*/
	}
}