/**
 * Generated by Gas3 v2.0.0 (Granite Data Services).
 *
 * NOTE: this file is only generated if it does not exist. You may safely put
 * your custom code here.
 */

package com.apolloti.getmarry.domain {

    [Bindable]
    [RemoteClass(alias="com.apolloti.getmarry.domain.Categoria")]
    public class Categoria extends CategoriaBase 
	{
		public function Categoria( nome:String=null )
		{
			super();
			this.nome = nome;
		}
    }
}