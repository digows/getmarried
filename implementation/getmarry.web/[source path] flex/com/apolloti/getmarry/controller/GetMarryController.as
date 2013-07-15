package com.apolloti.getmarry.controller
{
	import com.apolloti.getmarry.domain.Convidado;

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
		
		//Services
		
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
		}
			
		/*-------------------------------------------------------------------
		* 		 					GETTERS AND SETTERS
		*-------------------------------------------------------------------*/
	}
}