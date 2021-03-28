/**
 * Calls the Backend with an ajax Call to receive the Data for the selected Hashtag and then call changeTable to change the Visualisation
 * @param {String} hashtag - Hashtag to get Data
 */
function getData(hashtag){
    

    if(document.getElementById("leng").innerText.includes("Language"))
    {
        changeLen("All")
    }
    var requestData = {
    "hashtag": hashtag,
    "lang": document.getElementById("leng").innerText,
    "rt": document.getElementById("rtcheck").checked,
    "at": document.getElementById("atcheck").checked,
    "latin": document.getElementById("latincheck").checked
    };

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "data",
        dataType: 'json',
        data : requestData,

        success: function(response)
        {

            console.log('succes', response)
            changeTable(response)
        },
        error: function(response){
            console.log('error', response)
        }
        
    })

    }