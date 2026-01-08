"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  data() {
    return {
      filters: [
        { name: "全部", type: "" },
        { name: "国家级", type: "国家级" },
        { name: "省级", type: "省级" },
        { name: "市级", type: "市级" }
      ],
      currentFilter: 0,
      policyList: []
    };
  },
  onLoad() {
    this.loadPolicies();
  },
  methods: {
    loadPolicies() {
      let list = [...api_index.mockPolicies];
      const currentType = this.filters[this.currentFilter].type;
      if (currentType) {
        list = list.filter((item) => item.policyType === currentType);
      }
      this.policyList = list;
    },
    switchFilter(index) {
      this.currentFilter = index;
      this.loadPolicies();
    },
    goDetail(id) {
      common_vendor.index.navigateTo({
        url: "/pages/policy/detail?id=" + id
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: common_vendor.f($data.filters, (item, index, i0) => {
      return {
        a: common_vendor.t(item.name),
        b: index,
        c: $data.currentFilter === index ? 1 : "",
        d: common_vendor.o(($event) => $options.switchFilter(index), index)
      };
    }),
    b: common_vendor.f($data.policyList, (item, k0, i0) => {
      return common_vendor.e({
        a: common_vendor.t(item.policyTitle),
        b: item.isTop
      }, item.isTop ? {} : {}, {
        c: item.isRecommended && !item.isTop
      }, item.isRecommended && !item.isTop ? {} : {}, {
        d: common_vendor.t(item.policyType),
        e: common_vendor.t(item.issueDept),
        f: common_vendor.t(item.summary),
        g: common_vendor.t(item.issueDate),
        h: item.id,
        i: common_vendor.o(($event) => $options.goDetail(item.id), item.id)
      });
    }),
    c: $data.policyList.length === 0
  }, $data.policyList.length === 0 ? {} : {});
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-0c97cd6c"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/policy/list.js.map
