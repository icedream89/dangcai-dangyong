"use strict";
const common_vendor = require("../../common/vendor.js");
const store_user = require("../../store/user.js");
const _sfc_main = {
  data() {
    return {
      userInfo: null,
      isLoggedIn: false
    };
  },
  onLoad() {
    store_user.userStore.init();
    this.userInfo = store_user.userStore.state.userInfo;
    this.isLoggedIn = store_user.userStore.state.isLoggedIn;
  },
  onShow() {
    this.userInfo = store_user.userStore.state.userInfo;
    this.isLoggedIn = store_user.userStore.state.isLoggedIn;
  },
  methods: {
    navigateTo(url) {
      common_vendor.index.navigateTo({ url });
    },
    handleLogin() {
      common_vendor.index.navigateTo({
        url: "/pages/login/index"
      });
    },
    handleCollect() {
      common_vendor.index.showToast({
        title: "收藏功能开发中",
        icon: "none"
      });
    },
    handleAbout() {
      common_vendor.index.showModal({
        title: "关于我们",
        content: "当才当用\n当阳市科技经济信息商务局企业服务平台\n\nVersion 1.0.0",
        showCancel: false
      });
    },
    handleLogout() {
      common_vendor.index.showModal({
        title: "提示",
        content: "确定要退出登录吗？",
        success: (res) => {
          if (res.confirm) {
            store_user.userStore.logout();
            this.userInfo = null;
            this.isLoggedIn = false;
            common_vendor.index.showToast({
              title: "已退出登录",
              icon: "success"
            });
          }
        }
      });
    },
    // 格式化角色名称
    formatRole(role) {
      const roleMap = {
        "enterprise": "企业管理员",
        "employee": "企业员工",
        "normal": "普通用户"
      };
      return roleMap[role] || "未知角色";
    },
    // 判断是否为企业用户（管理员或员工）
    isEnterpriseUser() {
      return this.userInfo && (this.userInfo.role === "enterprise" || this.userInfo.role === "employee");
    },
    // 判断是否为普通用户
    isNormalUser() {
      return this.userInfo && this.userInfo.role === "normal";
    },
    // 企业管理功能
    handleManageProducts() {
      common_vendor.index.navigateTo({
        url: "/pages/enterprise/products"
      });
    },
    handleEditContact() {
      common_vendor.index.navigateTo({
        url: "/pages/enterprise/contact-edit"
      });
    },
    handleEditEnterprise() {
      common_vendor.index.showToast({
        title: "企业信息编辑功能开发中",
        icon: "none"
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: common_vendor.t($data.userInfo ? $data.userInfo.name : "未登录"),
    b: common_vendor.t($data.userInfo ? $options.formatRole($data.userInfo.role) : "普通用户"),
    c: !$data.userInfo
  }, !$data.userInfo ? {
    d: common_vendor.o((...args) => $options.handleLogin && $options.handleLogin(...args))
  } : {}, {
    e: $options.isEnterpriseUser()
  }, $options.isEnterpriseUser() ? {
    f: common_vendor.o((...args) => $options.handleManageProducts && $options.handleManageProducts(...args)),
    g: common_vendor.o((...args) => $options.handleEditContact && $options.handleEditContact(...args)),
    h: common_vendor.o((...args) => $options.handleEditEnterprise && $options.handleEditEnterprise(...args))
  } : {}, {
    i: common_vendor.o(($event) => $options.navigateTo("/pages/purchase/my-list")),
    j: common_vendor.o(($event) => $options.navigateTo("/pages/help/my-list")),
    k: common_vendor.o((...args) => $options.handleCollect && $options.handleCollect(...args)),
    l: common_vendor.o((...args) => $options.handleAbout && $options.handleAbout(...args)),
    m: $data.userInfo
  }, $data.userInfo ? {
    n: common_vendor.o((...args) => $options.handleLogout && $options.handleLogout(...args))
  } : {});
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-201c0da5"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/profile/index.js.map
