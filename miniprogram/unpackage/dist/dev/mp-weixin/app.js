"use strict";
Object.defineProperty(exports, Symbol.toStringTag, { value: "Module" });
const common_vendor = require("./common/vendor.js");
if (!Math) {
  "./pages/index/index.js";
  "./pages/login/index.js";
  "./pages/policy/list.js";
  "./pages/policy/detail.js";
  "./pages/course/list.js";
  "./pages/course/detail.js";
  "./pages/enterprise/list.js";
  "./pages/enterprise/detail.js";
  "./pages/enterprise/products.js";
  "./pages/enterprise/contact-edit.js";
  "./pages/help/submit.js";
  "./pages/help/my-list.js";
  "./pages/purchase/submit.js";
  "./pages/purchase/my-list.js";
  "./pages/profile/index.js";
}
const _sfc_main = {
  onLaunch: function() {
    common_vendor.index.__f__("log", "at App.vue:4", "App Launch");
  },
  onShow: function() {
    common_vendor.index.__f__("log", "at App.vue:7", "App Show");
  },
  onHide: function() {
    common_vendor.index.__f__("log", "at App.vue:10", "App Hide");
  }
};
function createApp() {
  const app = common_vendor.createSSRApp(_sfc_main);
  return {
    app
  };
}
createApp().app.mount("#app");
exports.createApp = createApp;
//# sourceMappingURL=../.sourcemap/mp-weixin/app.js.map
