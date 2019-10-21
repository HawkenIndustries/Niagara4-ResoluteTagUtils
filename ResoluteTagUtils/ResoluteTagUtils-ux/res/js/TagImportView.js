//Resolute Tag Import View Object

define(['nmodule/webEditors/rc/wb/mgr/Manager'], function(Manager){

    var TagImportView = function TagImportView (args) {
        Manager.call(this, {
            moduleName: 'ResoluteTagUtils',
            keyName: 'TagImportView'
        });
    }

    TagImportView.prototype.makeModel = function(component){
        return TagImportMgrModel.make(component);
    }
});