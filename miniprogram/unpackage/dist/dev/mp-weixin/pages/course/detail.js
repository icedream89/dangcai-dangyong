"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  data() {
    return {
      id: null,
      courseInfo: {}
    };
  },
  onLoad(options) {
    this.id = parseInt(options.id);
    this.loadDetail();
  },
  methods: {
    loadDetail() {
      this.courseInfo = api_index.mockCourses.find((c) => c.id === this.id) || {};
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
  return {
    a: $data.courseInfo.coverImage,
    b: common_vendor.t($data.courseInfo.title),
    c: common_vendor.t($data.courseInfo.category),
    d: common_vendor.t($data.courseInfo.author),
    e: common_vendor.t($data.courseInfo.viewCount),
    f: common_vendor.t($data.courseInfo.summary),
    g: common_vendor.t($data.courseInfo.content || "暂无详细内容"),
    h: common_vendor.o((...args) => $options.handleCollect && $options.handleCollect(...args)),
    i: common_vendor.o((...args) => $options.handleShare && $options.handleShare(...args))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-3d21314d"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/course/detail.js.map
