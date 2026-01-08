"use strict";
const common_vendor = require("../../common/vendor.js");
const store_user = require("../../store/user.js");
const _sfc_main = {
  data() {
    return {
      selectedRole: "",
      logging: false
    };
  },
  methods: {
    selectRole(role) {
      this.selectedRole = role;
    },
    async handleLogin() {
      if (!this.selectedRole) {
        return common_vendor.index.showToast({
          title: "请选择登录角色",
          icon: "none"
        });
      }
      this.logging = true;
      setTimeout(() => {
        const user = store_user.mockUsers.find((u) => u.role === this.selectedRole);
        if (user) {
          store_user.userStore.login(user);
          common_vendor.index.showToast({
            title: "登录成功",
            icon: "success"
          });
          setTimeout(() => {
            const pages = getCurrentPages();
            if (pages.length > 1) {
              common_vendor.index.navigateBack();
            } else {
              common_vendor.index.switchTab({
                url: "/pages/index/index"
              });
            }
          }, 1500);
        }
        this.logging = false;
      }, 1e3);
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $data.selectedRole === "enterprise"
  }, $data.selectedRole === "enterprise" ? {} : {}, {
    b: $data.selectedRole === "enterprise" ? 1 : "",
    c: common_vendor.o(($event) => $options.selectRole("enterprise")),
    d: $data.selectedRole === "employee"
  }, $data.selectedRole === "employee" ? {} : {}, {
    e: $data.selectedRole === "employee" ? 1 : "",
    f: common_vendor.o(($event) => $options.selectRole("employee")),
    g: $data.selectedRole === "normal"
  }, $data.selectedRole === "normal" ? {} : {}, {
    h: $data.selectedRole === "normal" ? 1 : "",
    i: common_vendor.o(($event) => $options.selectRole("normal")),
    j: common_vendor.t($data.logging ? "登录中..." : "微信授权登录"),
    k: common_vendor.o((...args) => $options.handleLogin && $options.handleLogin(...args)),
    l: !$data.selectedRole || $data.logging
  });
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-d08ef7d4"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/login/index.js.map
