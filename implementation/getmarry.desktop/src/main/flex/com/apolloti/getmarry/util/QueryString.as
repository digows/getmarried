package com.apolloti.getmarry.util
{
	import flash.external.*;
	import flash.utils.*;

	public class QueryString 
	{
		public static function get parameters():Object
		{
			var _params:Object = new Object();
			
			try
			{
				var _all:String =  ExternalInterface.call("window.location.href.toString");
				var _queryString:String = ExternalInterface.call("window.location.search.substring", 1);
				
				if( _queryString )
				{
					var params:Array = _queryString.split('&');
					var length:uint = params.length;
					
					for (var i:uint=0,index:int=-1; i<length; i++) 
					{
						var kvPair:String = params[i];
						
						if( (index = kvPair.indexOf("=")) > 0 )
						{
							var key:String = kvPair.substring(0,index);
							var value:String = kvPair.substring(index+1);
							_params[key] = value;
						}
					}
				}
			}
			catch(e:Error)
			{
				trace( e.message );
			}
			
			return _params;
		}		
	}
}
