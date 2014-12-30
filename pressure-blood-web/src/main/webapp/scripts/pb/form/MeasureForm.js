/*global define*/
define(["jquery"], function($) {
    return {
        validateAddMeasureForm : function() {
            $("#add-measure-form").validate({
                rules : {
                    sbp : {
                        required : true,
                        min : 0,
                        max : 300
                    },
                    dbp : {
                        required : true,
                        min : 0,
                        max : 300
                    },
                    pulse : {
                        required : false,
                        min : 0,
                        max : 300
                    },
                    datetimepicker : {
                        required : true
                    }
                },
                errorPlacement : function(error, element) {
                },
                highlight : function(element, errorClass, validClass) {
                    var label = $("label[for='" + element.id + "']");
                    $(label).css("color", "red");
                },
                unhighlight : function(element, errorClass, validClass) {
                    var label = $("label[for='" + element.id + "']");
                    $(label).css("color", "black");
                }
            });
        },
        isAddMeasureFormValid : function() {
            return $("#add-measure-form").valid();
        },
        clear : function() {
            $("#sbp").val("");
            $("#dbp").val("");
            $("#hand").val("LEFT_HAND");
            $("#pulse").val("");
            $("#datetimepicker").val("");
        },
        getSbp : function() {
            return $("#sbp").val();
        },
        getDbp : function() {
            return $("#dbp").val();
        },
        getDatetime : function() {
            return $("#datetimepicker").val();
        },
        getHand : function() {
            return $("#hand").val();
        },
        getPulse : function() {
            return $("#pulse").val();
        }
    };
});
