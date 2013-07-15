package com.apolloti.getmarry.controller
{
	import com.apolloti.getmarry.domain.Convidado;
	import com.apolloti.getmarry.ui.views.login.LoginForm;
	
	import mx.controls.Alert;
	
	import org.granite.tide.events.TideResultEvent;
	import org.granite.tide.events.TideUIEvent;

	[Name("loginController")]
	public class LoginController
	{
		/*-------------------------------------------------------------------
		* 		 					CONSTANTS
		*-------------------------------------------------------------------*/
		public static const VALIDATE_CODE:String = "State1";
		public static const REQUEST_CODE:String = "requestCode";
		public static const FILL_FIELDS:String = "fillFields";
		
		/*-------------------------------------------------------------------
		* 		 					ATTRIBUTES
		*-------------------------------------------------------------------*/
		//Model
		private var loginPopUp:LoginForm;
		
		[Bindable]
		public var currentState:String;
		
		//Controller
		[In]
		public var getMarryController:GetMarryController;
		[In]
		public var presenteSelectorController:PresenteSelectorController;
		
		//Services
		[In]
		public var convidadoService:Object;
		[In]
		public var notificacaoService:Object;
		
		/*-------------------------------------------------------------------
		* 		 					CONSTRUCTOR
		*-------------------------------------------------------------------*/
		public function LoginController()
		{
		}
		
		/*-------------------------------------------------------------------
		* 		 					BEHAVIORS
		*-------------------------------------------------------------------*/
		public function initialize():void
		{
		}
		
		public function verifyCodeAndLogon( convidado:Convidado ):void
		{
			this.convidadoService.verifyCode( convidado, verifyCodeResult );
			
			function verifyCodeResult( event:TideResultEvent ):void
			{
				getMarryController.convidado = Convidado(event.result);
				
				if ( getMarryController.convidado.needsToBeFilled )
				{
					currentState = FILL_FIELDS;
					dispatchEvent(new TideUIEvent("convidadoChanged", getMarryController.convidado ));
				}
				else
					presenteSelectorController.initialize();
			}
		}
		
		public function requestACode():void
		{
			this.currentState = REQUEST_CODE; 
			dispatchEvent(new TideUIEvent("convidadoChanged", new Convidado() ));
		}
		
		public function saveAndLogon( convidado:Convidado ):void
		{
			this.convidadoService.save( convidado, saveResult );
			
			function saveResult( event:TideResultEvent ):void
			{
				getMarryController.convidado = Convidado(event.result);
				presenteSelectorController.initialize();
			}
		}
		
		public function notifyCodeRequest( convidado:Convidado ):void
		{
			this.notificacaoService.notifyCodeRequest( convidado, notifyCodeRequestResult );
			
			function notifyCodeRequestResult( event:TideResultEvent ):void
			{
				currentState = VALIDATE_CODE;
				
				Alert.show("Seu código foi solicitado com sucesso! " +
					"\nPor favor aguarde até que você seja autorizado pelos noivos. " +
					"\nUm e-mail será enviado com o código para acesso quando autorizado.",
					"Código de acesso solicitado!");
			}
		}
		
		public function backToLogin():void
		{
			this.getMarryController.currentView = GetMarryController.LOGIN_VIEW;
			this.currentState = VALIDATE_CODE;
			dispatchEvent(new TideUIEvent("convidadoChanged", new Convidado() ));
		}
		
		/*-------------------------------------------------------------------
		* 		 					GETTERS AND SETTERS
		*-------------------------------------------------------------------*/
	}
}