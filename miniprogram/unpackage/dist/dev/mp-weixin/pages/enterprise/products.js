"use strict";
const common_vendor = require("../../common/vendor.js");
const store_user = require("../../store/user.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  data() {
    return {
      myProducts: []
    };
  },
  onLoad() {
    this.loadProducts();
  },
  methods: {
    loadProducts() {
      const enterpriseId = store_user.userStore.getEnterpriseId();
      if (enterpriseId) {
        this.myProducts = api_index.mockProducts.filter((p) => p.enterpriseId === enterpriseId);
      }
    },
    handleAdd() {
      common_vendor.index.showToast({
        title: "产品发布功能开发中",
        icon: "none"
      });
    },
    handleEdit(product) {
      common_vendor.index.showToast({
        title: "产品编辑功能开发中",
        icon: "none"
      });
    },
    handleDelete(product) {
      common_vendor.index.showModal({
        title: "确认删除",
        content: `确定要删除"${product.productName}"吗？`,
        success: (res) => {
          if (res.confirm) {
            common_vendor.index.showToast({
              title: "删除成功",
              icon: "success"
            });
            this.loadProducts();
          }
        }
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: common_vendor.o((...args) => $options.handleAdd && $options.handleAdd(...args)),
    b: common_vendor.f($data.myProducts, (item, k0, i0) => {
      return {
        a: item.coverImage,
        b: common_vendor.t(item.productName),
        c: common_vendor.t(item.category),
        d: common_vendor.t(item.price),
        e: common_vendor.t(item.unit),
        f: common_vendor.t(item.description),
        g: common_vendor.t(item.salesCount),
        h: common_vendor.t(item.status === "online" ? "已上架" : "已下架"),
        i: common_vendor.o(($event) => $options.handleEdit(item), item.id),
        j: common_vendor.o(($event) => $options.handleDelete(item), item.id),
        k: item.id,
        l: common_vendor.o(($event) => $options.handleEdit(item), item.id)
      };
    }),
    c: $data.myProducts.length === 0
  }, $data.myProducts.length === 0 ? {} : {});
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-b1e4e92e"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/enterprise/products.js.map
