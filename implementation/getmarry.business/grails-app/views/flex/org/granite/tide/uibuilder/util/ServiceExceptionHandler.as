package org.granite.tide.uibuilder.util
{
	import mx.controls.Alert;
	import mx.messaging.messages.ErrorMessage;
	
	import org.granite.tide.BaseContext;
	import org.granite.tide.DefaultExceptionHandler;

	public class ServiceExceptionHandler extends DefaultExceptionHandler
	{
		public function ServiceExceptionHandler()
		{
		}
		
		override public function handle( context:BaseContext, error:ErrorMessage ):void 
		{
			if ( error.faultString != null && error.faultString != "" )
			{
				Alert.show( error.faultString, "Ops! Ocorreu um problema." );
			}
			else if ( error.faultDetail != null && error.faultDetail != "" )
			{
				Alert.show( error.faultDetail, "Ops! Ocorreu um problema." );
			}
			else if ( error.faultCode != null && error.faultCode != "" )
			{
				Alert.show( error.faultCode, "Ops! Ocorreu um problema." );	
			}
		}
	}
}