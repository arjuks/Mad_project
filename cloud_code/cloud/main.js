
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

Parse.Cloud.afterSave("Album", function(request) {
  var pushQuery = new Parse.Query(Parse.Installation);
  // var userQuery = new Parse.Query(Parse.User);
  // userQuery.equalTo()

  // pushQuery.matchesQuery('user', userQuery);
  Parse.Push.send({
    where: pushQuery, // Set our Installation query
    data: {
      alert: "Album Updated!"
    }
  }, {
    success: function() {
      console.log('push went successfully');
      // Push was successful
    },
    error: function(error) {
      console.log('push had an error ');
      console.log(error);
      // Handle error
    }
  });
})

Parse.Cloud.afterSave("_User", function(request) {
  var pushQuery = new Parse.Query(Parse.Installation);
  // var userQuery = new Parse.Query(Parse.User);
  // userQuery.equalTo()

  // pushQuery.matchesQuery('user', userQuery);
  Parse.Push.send({
    where: pushQuery, // Set our Installation query
    data: {
      alert: "New User Saved!"
    }
  }, {
    success: function() {
      console.log('push went successfully');
      // Push was successful
    },
    error: function(error) {
      console.log('push had an error ');
      console.log(error);
      // Handle error
    }
  });
})
// curl -X POST \
//  -H "X-Parse-Application-Id: VQkjcPKUPzUNBjZOFP3OP9MolVCHPDzWODGzbYDS" \
//  -H "X-Parse-REST-API-Key: 6nOYZv37gLfT8EE7NhsRXBgSwixsLwJunUGQEzka" \
//  -H "Content-Type: application/json" \
//  -d '{}' \
//  https://api.parse.com/1/functions/hello
