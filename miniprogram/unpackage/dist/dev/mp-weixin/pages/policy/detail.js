"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  data() {
    return {
      id: null,
      policyInfo: {}
    };
  },
  onLoad(options) {
    this.id = parseInt(options.id);
    this.loadDetail();
  },
  methods: {
    loadDetail() {
      this.policyInfo = api_index.mockPolicies.find((p) => p.id === this.id) || {};
    },
    handleCollect() {
      common_vendor.index.showToast({
        title: "收藏成功",
        icon: "success"
      });
    },
    handleShare() {
      common_vendor.index.showShareMenu({
        withShareTicket: true
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: common_vendor.t($data.policyInfo.policyTitle),
    b: $data.policyInfo.isTop
  }, $data.policyInfo.isTop ? {} : {}, {
    c: $data.policyInfo.isRecommended
  }, $data.policyInfo.isRecommended ? {} : {}, {
    d: common_vendor.t($data.policyInfo.policyNo),
    e: common_vendor.t($data.policyInfo.policyType),
    f: common_vendor.t($data.policyInfo.issueDept),
    g: common_vendor.t($data.policyInfo.issueDate),
    h: common_vendor.t($data.policyInfo.summary),
    i: common_vendor.t($data.policyInfo.content),
    j: common_vendor.o((...args) => $options.handleCollect && $options.handleCollect(...args)),
    k: common_vendor.o((...args) => $options.handleShare && $options.handleShare(...args))
  });
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-059a76ff"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/policy/detail.js.map
