//Tag Import View's Model Object

var RESOLUTE_TAG_IMPORT_TYPES = ["ResoluteTagUtils:BPoint"];
var TagImportModel = function TagImportModel(){
    MgrModel.apply(this, arguments);
};
TagImportModel.prototype = Object.create(MgrModel.prototype);
TagImportModel.prototype.constructor = MyMgrModel;

/**@returns {Promise.<MgrModel>}**/
TagImportModel.make = function(component){
    return MgrTypeInfo.make(RESOLUTE_TAG_IMPORT_TYPES)
        .then(function(newTypes){
            return new TagImportModel({
                columns: makeColumns(),
                componentSource: component,
                newTypes: newTypes
            });
        });
};