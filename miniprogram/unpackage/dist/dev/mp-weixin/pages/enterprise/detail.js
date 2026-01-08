"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  data() {
    return {
      id: null,
      enterprise: {},
      productList: []
    };
  },
  onLoad(options) {
    this.id = parseInt(options.id);
    this.loadDetail();
  },
  methods: {
    loadDetail() {
      this.enterprise = api_index.mockEnterprises.find((e) => e.id === this.id) || {};
      this.productList = api_index.mockProducts.filter((p) => p.enterpriseId === this.id);
    },
    previewImage(index) {
      common_vendor.index.previewImage({
        urls: this.enterprise.images,
        current: index
      });
    },
    handleCall() {
      common_vendor.index.makePhoneCall({
        phoneNumber: "13800138000"
      });
    },
    handleCollect() {
      common_vendor.index.showToast({
        title: "收藏成功",
        icon: "success"
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $data.enterprise.logo,
    b: common_vendor.t($data.enterprise.enterpriseName),
    c: common_vendor.t($data.enterprise.industry),
    d: common_vendor.t($data.enterprise.scale),
    e: common_vendor.t($data.enterprise.intro),
    f: $data.enterprise.images && $data.enterprise.images.length > 0
  }, $data.enterprise.images && $data.enterprise.images.length > 0 ? {
    g: common_vendor.f($data.enterprise.images, (img, idx, i0) => {
      return {
        a: idx,
        b: img,
        c: common_vendor.o(($event) => $options.previewImage(idx), idx)
      };
    })
  } : {}, {
    h: common_vendor.t($data.productList.length),
    i: common_vendor.f($data.productList, (item, k0, i0) => {
      return {
        a: item.coverImage,
        b: common_vendor.t(item.productName),
        c: common_vendor.t(item.price),
        d: common_vendor.t(item.unit),
        e: common_vendor.t(item.salesCount),
        f: item.id
      };
    }),
    j: common_vendor.t($data.enterprise.legalPerson),
    k: common_vendor.t($data.enterprise.address),
    l: common_vendor.o((...args) => $options.handleCall && $options.handleCall(...args)),
    m: common_vendor.o((...args) => $options.handleCollect && $options.handleCollect(...args))
  });
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-05b5605a"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/enterprise/detail.js.map
