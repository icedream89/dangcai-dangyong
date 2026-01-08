"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  data() {
    return {
      banners: [
        {
          image: "https://images.unsplash.com/photo-1497366216548-37526070297c?w=800",
          title: "当才当用 - 企业服务平台"
        },
        {
          image: "https://images.unsplash.com/photo-1551434678-e076c223a692?w=800",
          title: "助力企业发展"
        },
        {
          image: "https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?w=800",
          title: "政策精准推送"
        }
      ],
      policies: [],
      recommendedEnterprises: [],
      hotCourses: []
    };
  },
  onLoad() {
    this.loadData();
  },
  methods: {
    async loadData() {
      this.policies = api_index.mockPolicies.slice(0, 3);
      this.recommendedEnterprises = api_index.mockEnterprises.filter((e) => e.isRecommended);
      this.hotCourses = api_index.mockCourses.filter((c) => c.isRecommended);
    },
    navigateTo(url) {
      common_vendor.index.navigateTo({ url });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.f($data.banners, (item, index, i0) => {
      return {
        a: item.image,
        b: common_vendor.t(item.title),
        c: index
      };
    }),
    b: common_vendor.o(($event) => $options.navigateTo("/pages/policy/list")),
    c: common_vendor.o(($event) => $options.navigateTo("/pages/course/list")),
    d: common_vendor.o(($event) => $options.navigateTo("/pages/enterprise/list")),
    e: common_vendor.o(($event) => $options.navigateTo("/pages/purchase/submit")),
    f: common_vendor.o(($event) => $options.navigateTo("/pages/help/submit")),
    g: common_vendor.o(($event) => $options.navigateTo("/pages/policy/list")),
    h: common_vendor.f($data.policies, (item, k0, i0) => {
      return common_vendor.e({
        a: item.isTop
      }, item.isTop ? {} : {}, {
        b: common_vendor.t(item.policyTitle),
        c: common_vendor.t(item.policyType),
        d: common_vendor.t(item.issueDate),
        e: item.id,
        f: common_vendor.o(($event) => $options.navigateTo("/pages/policy/detail?id=" + item.id), item.id)
      });
    }),
    i: common_vendor.o(($event) => $options.navigateTo("/pages/enterprise/list")),
    j: common_vendor.f($data.recommendedEnterprises, (item, k0, i0) => {
      return {
        a: item.logo,
        b: common_vendor.t(item.enterpriseName),
        c: common_vendor.t(item.industry),
        d: common_vendor.f(item.tags, (tag, idx, i1) => {
          return {
            a: common_vendor.t(tag),
            b: idx
          };
        }),
        e: item.id,
        f: common_vendor.o(($event) => $options.navigateTo("/pages/enterprise/detail?id=" + item.id), item.id)
      };
    }),
    k: common_vendor.o(($event) => $options.navigateTo("/pages/course/list")),
    l: common_vendor.f($data.hotCourses, (item, k0, i0) => {
      return {
        a: item.coverImage,
        b: common_vendor.t(item.title),
        c: common_vendor.t(item.category),
        d: common_vendor.t(item.viewCount),
        e: item.id,
        f: common_vendor.o(($event) => $options.navigateTo("/pages/course/detail?id=" + item.id), item.id)
      };
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-1cf27b2a"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/index/index.js.map
