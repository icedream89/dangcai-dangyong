"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  data() {
    return {
      searchText: "",
      selectedCategoryId: 1,
      // 默认选中"全部"
      categories: [],
      enterpriseList: []
    };
  },
  computed: {
    // 根据选中的分类和搜索词过滤企业列表
    filteredEnterpriseList() {
      var _a;
      let result = [...this.enterpriseList];
      if (this.selectedCategoryId !== 1) {
        const categoryName = (_a = this.categories.find((c) => c.id === this.selectedCategoryId)) == null ? void 0 : _a.name;
        result = result.filter((e) => e.industry === categoryName);
      }
      if (this.searchText) {
        result = result.filter(
          (e) => e.enterpriseName.includes(this.searchText)
        );
      }
      return result;
    }
  },
  onLoad() {
    this.loadCategories();
    this.loadEnterprises();
  },
  methods: {
    loadCategories() {
      this.categories = [...api_index.mockEnterpriseCategories];
    },
    loadEnterprises() {
      this.enterpriseList = [...api_index.mockEnterprises];
    },
    selectCategory(id) {
      this.selectedCategoryId = id;
    },
    handleSearch() {
    },
    goDetail(id) {
      common_vendor.index.navigateTo({
        url: "/pages/enterprise/detail?id=" + id
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: common_vendor.o((...args) => $options.handleSearch && $options.handleSearch(...args)),
    b: $data.searchText,
    c: common_vendor.o(($event) => $data.searchText = $event.detail.value),
    d: common_vendor.f($data.categories, (item, k0, i0) => {
      return {
        a: common_vendor.t(item.icon),
        b: common_vendor.t(item.name),
        c: common_vendor.t(item.count),
        d: $data.selectedCategoryId === item.id ? 1 : "",
        e: item.id,
        f: common_vendor.o(($event) => $options.selectCategory(item.id), item.id)
      };
    }),
    e: common_vendor.f($options.filteredEnterpriseList, (item, k0, i0) => {
      return common_vendor.e({
        a: item.logo,
        b: common_vendor.t(item.enterpriseName),
        c: common_vendor.t(item.industry),
        d: common_vendor.t(item.scale),
        e: item.isRecommended
      }, item.isRecommended ? {} : {}, {
        f: common_vendor.t(item.intro),
        g: common_vendor.f(item.tags, (tag, idx, i1) => {
          return {
            a: common_vendor.t(tag),
            b: idx
          };
        }),
        h: item.id,
        i: common_vendor.o(($event) => $options.goDetail(item.id), item.id)
      });
    }),
    f: $options.filteredEnterpriseList.length === 0
  }, $options.filteredEnterpriseList.length === 0 ? {} : {});
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-88fede7c"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/enterprise/list.js.map
