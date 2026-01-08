"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  data() {
    return {
      categories: ["政策解读", "财务税务", "数字化"],
      currentCategory: "",
      courseList: []
    };
  },
  onLoad() {
    this.loadCourses();
  },
  methods: {
    loadCourses() {
      let list = [...api_index.mockCourses];
      if (this.currentCategory) {
        list = list.filter((item) => item.category === this.currentCategory);
      }
      this.courseList = list;
    },
    switchCategory(category) {
      this.currentCategory = category;
      this.loadCourses();
    },
    goDetail(id) {
      common_vendor.index.navigateTo({
        url: "/pages/course/detail?id=" + id
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.currentCategory === "" ? 1 : "",
    b: common_vendor.o(($event) => $options.switchCategory("")),
    c: common_vendor.f($data.categories, (cat, k0, i0) => {
      return {
        a: common_vendor.t(cat),
        b: cat,
        c: $data.currentCategory === cat ? 1 : "",
        d: common_vendor.o(($event) => $options.switchCategory(cat), cat)
      };
    }),
    d: common_vendor.f($data.courseList, (item, k0, i0) => {
      return {
        a: item.coverImage,
        b: common_vendor.t(item.title),
        c: common_vendor.t(item.courseType),
        d: common_vendor.t(item.summary),
        e: common_vendor.t(item.author),
        f: common_vendor.t(item.viewCount),
        g: item.id,
        h: common_vendor.o(($event) => $options.goDetail(item.id), item.id)
      };
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-80d79958"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/course/list.js.map
