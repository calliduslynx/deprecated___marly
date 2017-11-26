(function () {

  /**
   * Sobald man einen "ng-include" tag verwendet, schaltet angular automatisch auf sein routing um.
   * Hierdurch wird index.html#abc ungefragt zu index.html#/abc ... die folgenden Zeilen verhindern das
   * SO: 23299966 und 18611214 @formatter:off
   */
  var app = angular.module("MarlyApp", []);
  app.controller("MainController", function ($scope, $http) {
    vm = this;

    vm.userdetails = null;
    vm.mappingdetails = null;
    vm.newUrl = "";

    var loadUserDetails = function () {
      $http.get("/user").then(function (response) {
        if (response.data) {
          vm.userdetails = response.data;
          loadMappingDetails();
        }
      });
    };

    var loadMappingDetails = function () {
      if (vm.userdetails === null)
        return;

      $http.get("/mappings").then(function (response) {
        if (response.data) {
          vm.mappingdetails = response.data;
          vm.mappingdetails.forEach(function (details) {
            $http.get("/statistic/" + details.tiny).then(function (result) {
              details.statistic = result.data;
            });
          });
        }
      });
    };

    vm.createNewMapping = function () {
      if (!vm.newUrl.startsWith("http")) {
        alert("please specify url starting with http or https");
        return;
      }

      $http.post("/mappings", {url: vm.newUrl}).then(function (response) {
        loadMappingDetails();
      });
    };

    vm.deleteMapping = function (tiny) {
      $http.delete("/mappings/" + tiny).then(function () {
        loadMappingDetails();
      });
    };

    loadUserDetails();

    setInterval(function () {
      loadMappingDetails()
    }, 1500);

  });
})();