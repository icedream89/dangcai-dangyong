"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      formData: {
        title: "",
        budget: "",
        expectDate: "",
        content: "",
        phone: ""
      },
      submitting: false
    };
  },
  methods: {
    onDateChange(e) {
      this.formData.expectDate = e.detail.value;
    },
    async handleSubmit() {
      if (!this.formData.title) {
        return common_vendor.index.showToast({ title: "请输入需求标题", icon: "none" });
      }
      if (!this.formData.content) {
        return common_vendor.index.showToast({ title: "请输入需求描述", icon: "none" });
      }
      this.submitting = true;
      setTimeout(() => {
        this.submitting = false;
        common_vendor.index.showToast({
          title: "提交成功",
          icon: "success"
        });
        setTimeout(() => {
          common_vendor.index.navigateTo({
            url: "/pages/purchase/my-list"
          });
        }, 1500);
      }, 1e3);
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.formData.title,
    b: common_vendor.o(($event) => $data.formData.title = $event.detail.value),
    c: $data.formData.budget,
    d: common_vendor.o(($event) => $data.formData.budget = $event.detail.value),
    e: common_vendor.t($data.formData.expectDate || "请选择期望交付日期"),
    f: !$data.formData.expectDate ? 1 : "",
    g: common_vendor.o((...args) => $options.onDateChange && $options.onDateChange(...args)),
    h: $data.formData.content,
    i: common_vendor.o(($event) => $data.formData.content = $event.detail.value),
    j: common_vendor.t($data.formData.content.length),
    k: $data.formData.phone,
    l: common_vendor.o(($event) => $data.formData.phone = $event.detail.value),
    m: common_vendor.t($data.submitting ? "提交中..." : "提交需求"),
    n: common_vendor.o((...args) => $options.handleSubmit && $options.handleSubmit(...args)),
    o: $data.submitting
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-1a473c11"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/purchase/submit.js.map
